package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
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
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyLoadStateAdapter
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyPagingAdapter
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Default
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Loading
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.NetworkError
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.NothingFound
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.SearchResults
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.ServerError
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.formatNumber
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    private val adapter = VacancyPagingAdapter { vacancy ->
        if (clickDebounce()) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.id))
        }
    }
    private val loadStateAdapter = VacancyLoadStateAdapter()
    private var isClickAllowed = true
    private var lastPagingData: PagingData<Vacancy>? = null

    override fun initViews() {
        // инициализируем наши вьюхи тут
        isClickAllowed = true
        setupSearchTextWatcher()
        setupClearButtonClickListener()
        setEditTextActionListener()
        setRecyclerView()
    }

    override fun subscribe() {
        // подписка на данные от viewModel
        with(viewModel) {
            getSearchState().observe(viewLifecycleOwner) { state ->
                renderScreen(state)
            }

            getFoundCount().observe(viewLifecycleOwner) { foundCount ->
                showFoundCount(foundCount)
            }

            getPagingDataLiveData().observe(viewLifecycleOwner) { pagingData ->
                refreshData(pagingData)
            }
        }
    }

    private fun refreshData(pagingData: PagingData<Vacancy>) {
        if (pagingData != lastPagingData) { // проверяем, обновились ли данные
            lifecycleScope.launch {
                lastPagingData = pagingData
                adapter.clear() // Принудительно очищаем адаптер
                delay(SHOW_RECYCLER_DELAY) // Небольшая задержка для корректного обновления UI
                adapter.submitData(lifecycle, pagingData) // Загружаем новые данные
            }
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateClearButtonIcon(text)
                if (!text.isNullOrEmpty()) {
                    viewModel.searchWithDebounce(text.toString())
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
            viewModel.cancelSearchDebounce()
            viewModel.setDefaultScreen()
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

    private fun setRecyclerView() {
        binding.recycler.apply {
            adapter = this@SearchFragment.adapter.withLoadStateFooter(footer = loadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }
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
                }
            }
        }
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

    private fun renderScreen(state: SearchScreenState) {
        Log.d("DEBUG", "SearchScreenState = $state")
        placeholderVisibilityManager(isError = state is Error)
        notificationVisibilityManager(needToBeVisible = state is SearchResults || state is NothingFound)
        defaultScreenVisibilityManager(needToBeVisible = state is Default)
        recyclerVisibilityManager(needToBeVisible = state is SearchResults)
        progressBarVisibilityManager(isLoading = state is Loading)

        if (state is Error) {
            placeholderContentManager(state)
        }

    }

    // обработка разных типов ошибок
    private fun placeholderContentManager(state: Error) {
        binding.apply {
            when (state) {
                is NetworkError -> {
                    ivErrorImage.setImageResource(R.drawable.placeholder_network_error)
                    tvErrorText.text = getString(R.string.error_no_internet)
                }

                is NothingFound -> {
                    ivErrorImage.setImageResource(R.drawable.placeholder_not_found)
                    tvErrorText.text = getString(R.string.error_nothing_found)
                }

                is ServerError -> {
                    ivErrorImage.setImageResource(R.drawable.placeholder_server_error_search)
                    tvErrorText.text = getString(R.string.error_server)
                }
            }
        }
    }

    private fun clearAdapter() {
        adapter.submitData(lifecycle, PagingData.empty())
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

    private fun notificationVisibilityManager(needToBeVisible: Boolean) {
        if (!needToBeVisible) {
            binding.notificationText.hide()
        }
    }

    private fun defaultScreenVisibilityManager(needToBeVisible: Boolean) {
        binding.ivPlaceholderMain.isVisible = needToBeVisible
    }

    private fun recyclerVisibilityManager(needToBeVisible: Boolean) {
        binding.recycler.isVisible = needToBeVisible
    }

    private fun placeholderVisibilityManager(isError: Boolean) {
        binding.llErrorContainer.isVisible = isError
    }

    private fun progressBarVisibilityManager(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading

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
        private const val SHOW_RECYCLER_DELAY = 200L

    }
}
