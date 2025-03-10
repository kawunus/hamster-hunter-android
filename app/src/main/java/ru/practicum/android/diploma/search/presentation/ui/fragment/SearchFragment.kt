package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.annotation.SuppressLint
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.data.network.exception.NoInternetException
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.Default
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.Loading
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.NetworkError
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.NothingFound
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.SearchResults
import ru.practicum.android.diploma.search.presentation.model.SearchScreenState.ServerError
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyLoadStateAdapter
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyPagingAdapter
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_BUNDLE_KEY
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_REQUEST_KEY
import ru.practicum.android.diploma.util.NetworkMonitor
import ru.practicum.android.diploma.util.formatNumber
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModel()

    private val adapter = VacancyPagingAdapter { vacancy ->
        if (clickDebounce()) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.id))
        }
    }
    private val loadStateAdapter = VacancyLoadStateAdapter()
    private var isClickAllowed = true
    private var lastPagingData: PagingData<Vacancy>? = null

    override fun initViews() {
        isClickAllowed = true
        setupSearchTextWatcher()
        setupClearButtonClickListener()
        setEditTextActionListener()
        setRecyclerView()
    }

    override fun subscribe() {
        with(viewModel) {
            getSearchState().observe(viewLifecycleOwner) { renderScreen(it) }

            getFoundCount().observe(viewLifecycleOwner) { showFoundCount(it) }

            getAnyFilterApplied().observe(viewLifecycleOwner) { renderFilterButton(it) }
        }
        // подписка на изменения в FilterFragment
        parentFragmentManager.setFragmentResultListener(
            FILTERS_CHANGED_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val filtersChanged = bundle.getBoolean(FILTERS_CHANGED_BUNDLE_KEY)
            if (filtersChanged && viewModel.getSearchState().value != Default) {
                viewModel.startSearchWithLatestText()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIfAnyFilterApplied()
        hideNotificationIfNoNeedIt()
    }

    private fun refreshData(pagingData: PagingData<Vacancy>) {
        if (pagingData != lastPagingData) { // проверяем, обновились ли данные
            lifecycleScope.launch {
                lastPagingData = pagingData
                adapter.clear() // Принудительно очищаем адаптер
                adapter.submitData(lifecycle, pagingData)
                adapter.notifyDataSetChanged()
                showRecycler()
            }
        }
    }

    private fun showRecycler() {
        with(binding) {
            llErrorContainer.hide()
            ivPlaceholderMain.hide()
            progressBar.hide()
            recycler.show()
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateClearButtonIcon(text)
                if (!text.isNullOrEmpty()) {
                    viewModel.searchWithDebounce(text.toString())
                } else {
                    setDefaultScreen()
                }
            },
        )
    }

    // обновление иконки кнопки
    private fun updateClearButtonIcon(text: CharSequence?) {
        binding.buttonClear.setImageResource(
            if (text?.isNotEmpty() == true) {
                R.drawable.edit_text_clear_button
            } else {
                R.drawable.ic_search
            }
        )
    }

    // настройка слушателя нажатий
    private fun setupClearButtonClickListener() {
        binding.buttonClear.setOnClickListener {
            handleClearButtonClick()
            setDefaultScreen()
        }
        binding.buttonFilter.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment())
        }
    }

    // обработка нажатия на кнопку очистки
    private fun handleClearButtonClick() {
        if (binding.edittextSearch.text.isNotEmpty()) {
            binding.edittextSearch.text.clear()
        }
    }

    // поиск по кнопке на системной клавиатуре, чтобы не заставлять юзера ждать 2 сек
    private fun setEditTextActionListener() {
        binding.edittextSearch.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE && text.isNotEmpty()) {
                    lifecycleScope.launch {
                        viewModel.cancelSearchDebounce()
                        clearAdapter()
                        viewModel.startSearch(text.toString())
                    }
                }
                false
            }
        }
    }

    private fun setDefaultScreen() = with(viewModel) {
        cancelSearchDebounce()
        setDefaultScreen()
    }

    private fun setRecyclerView() = with(binding.recycler) {
        adapter = this@SearchFragment.adapter.withLoadStateFooter(footer = loadStateAdapter)
        layoutManager = LinearLayoutManager(requireContext())
        setLoadStateListener()
    }

    private fun setLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            when {
                // ошибки при первичной загрузке контента
                loadState.refresh is LoadState.Error -> {
                    viewModel.setErrorScreenState(loadState.refresh as LoadState.Error)
                }
                // ошибки при загрузке страниц
                loadState.append is LoadState.Error || loadState.prepend is LoadState.Error -> {
                    showPagingError(loadState.append as? LoadState.Error ?: loadState.prepend as LoadState.Error)
                    lifecycleScope.launch {
                        waitForInternetAndRetry()
                    }
                }
            }
        }
    }

    private fun renderScreen(state: SearchScreenState) {
        when (state) {
            is Default -> showDefaultScreen()
            is Error -> showError(state)
            is Loading -> showLoading()
            is SearchResults -> showSearchResults(state.data)
        }
    }

    // обработка разных типов ошибок
    private fun placeholderContentManager(state: Error) = with(binding) {
        when (state) {
            is NetworkError -> {
                ivErrorImage.setImageResource(R.drawable.placeholder_network_error)
                tvErrorText.text = getString(R.string.error_no_internet)
            }

            is NothingFound -> {
                ivErrorImage.setImageResource(R.drawable.placeholder_not_found)
                tvErrorText.text = getString(R.string.error_nothing_found_vacancy)
                notificationText.show()
            }

            is ServerError -> {
                ivErrorImage.setImageResource(R.drawable.placeholder_server_error_search)
                tvErrorText.text = getString(R.string.error_server)
            }
        }
    }

    private fun showDefaultScreen() = with(binding) {
        llErrorContainer.hide()
        notificationText.hide()
        ivPlaceholderMain.show()
        recycler.hide()
        progressBar.hide()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResults(data: PagingData<Vacancy>) {
        lifecycleScope.launch {
            refreshData(data)
        }
    }

    private fun showLoading() = with(binding) {
        llErrorContainer.hide()
        notificationText.hide()
        ivPlaceholderMain.hide()
        recycler.hide()
        progressBar.show()
    }

    private fun showError(state: Error) = with(binding) {
        llErrorContainer.show()
        notificationText.hide()
        ivPlaceholderMain.hide()
        recycler.hide()
        progressBar.hide()

        placeholderContentManager(state)
    }

    // отображение сообщения "Найдено $count вакансий"
    private fun showFoundCount(count: Int?) {
        binding.notificationText.apply {
            if (count == null) {
                hide()
            } else {
                text = if (count == 0) {
                    getString(R.string.no_such_jobs)
                } else {
                    resources.getQuantityString(R.plurals.found_jobs_plural, count, formatNumber(count))
                }
                show()
            }
        }
    }

    private fun hideNotificationIfNoNeedIt() {
        binding.notificationText.apply {
            if (viewModel.getSearchState().value == Default) {
                hide()
            }
        }
    }

    private fun renderFilterButton(anyFilterApplied: Boolean?) {
        val iconRes = when (anyFilterApplied) {
            true -> R.drawable.ic_filter_on
            else -> R.drawable.ic_filter_off
        }
        binding.buttonFilter.setImageDrawable(getDrawable(requireContext(), iconRes))
    }

    private fun clearAdapter() {
        adapter.submitData(lifecycle, PagingData.empty())
    }

    private fun showPagingError(error: LoadState.Error) {
        val message = when (error.error) {
            is NoInternetException -> getString(R.string.error_toast_no_internet)
            else -> getString(R.string.error_toast_server)
        }
        showToast(message)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private suspend fun waitForInternetAndRetry() {
        while (!NetworkMonitor.isNetworkAvailable(requireContext())) {
            delay(RETRY_LOADING_DELAY)
        }
        adapter.retry() // Повторяем загрузку, когда интернет появился
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val RETRY_LOADING_DELAY = 2000L
    }
}
