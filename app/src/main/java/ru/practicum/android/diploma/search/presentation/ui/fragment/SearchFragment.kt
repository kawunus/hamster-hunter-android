package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Default
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.Loading
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.NetworkError
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.NothingFound
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.SearchResults
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState.ServerError
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    private val adapter = VacancyAdapter { vacancy ->
        if (clickDebounce()) {
            // !!! Обрабаботка клика по вакансии должна быть ТУТ !!!
        }
    }
    private var isClickAllowed = true

    override fun initViews() {
        // init views
        isClickAllowed = true
        setupSearchTextWatcher()
        setupClearButtonClickListener()
        setEditTextActionListener()
        setRecyclerView()
    }

    override fun subscribe() {
        // subscribe on viewModel
        with(viewModel) {
            getSearchState().observe(viewLifecycleOwner) { state ->
                renderScreen(state)
            }

            getFoundCount().observe(viewLifecycleOwner) { foundCount ->
                showFoundCount(foundCount)
                Log.d("DEBUG foundCount", "Fragment observe -> Всего вакансий найдено: $foundCount")
            }

            getIsNextPageLoading().observe(viewLifecycleOwner) { isLoading ->
                progressBarVisibilityManager(isLoading)
            }
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { text, start, before, count ->
                updateClearButtonIcon(text)

                if (!text.isNullOrEmpty()) viewModel.searchWithDebounce(text.toString())
            },
            afterTextChanged = { _ -> }
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
                    viewModel.cancelSearchDebounce()
                    viewModel.startSearch(text.toString())
                }
                false
            }
        }
    }

    private fun setRecyclerView() {
        binding.recycler.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.addLoadStateListener { loadStates ->
            viewModel.setNextPageLoading(loadStates.append is LoadState.Loading)
        }
    }

    private fun renderScreen(state: SearchScreenState) {
        Log.d("DEBUG", "SearchScreenState = $state")

        // Общие настройки видимости
        placeholderVisibilityManager(isError = state is Error)
        notificationVisibilityManager(needToBeVisible = state is SearchResults || state is NothingFound)
        defaultScreenVisibilityManager(needToBeVisible = state is Default)
        recyclerVisibilityManager(needToBeVisible = state is SearchResults)
        progressBarVisibilityManager(isLoading = state is Loading)

        // Уникальная логика для каждого состояния
        when (state) {
            is Error -> placeholderContentManager(state)
            is SearchResults -> showSearchResults(state.pagingData)
            else -> Unit // Ничего не делаем для остальных состояний
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

    // отображение сообщения "Найдено $count вакансий"
    private fun showFoundCount(count: Int) {
        binding.notificationText.apply {
            text = if (count == 0) {
                getString(R.string.no_such_jobs)
            } else {
                resources.getQuantityString(R.plurals.found_jobs_plural, count, count)
            }
            isVisible = true
        }
    }

    private fun notificationVisibilityManager(needToBeVisible: Boolean) {
        if (!needToBeVisible) {
            binding.notificationText.isVisible = false
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

    private fun showSearchResults(pagingData: PagingData<Vacancy>) {
        adapter.submitData(lifecycle, pagingData)
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
    }
}
