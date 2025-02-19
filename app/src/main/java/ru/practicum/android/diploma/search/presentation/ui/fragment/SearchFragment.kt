package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
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
        isClickAllowed = true
        setupSearchTextWatcher()
        setupClearButtonClickListener()
        setEditTextActionListener()
        setRecyclerView()
    }


    override fun subscribe() {
        viewModel.getSearchState().observe(viewLifecycleOwner)
        { state ->
            renderScreen(state)
            hideProgressBar(state)
        }

        viewModel.getFoundCount().observe(viewLifecycleOwner) { foundCount ->
            showFoundCount(foundCount)     // информация из репозитория пока не передаётся сюда нормально, но обрабатывать значение лайвдаты уже можно. Скоро всё починю.
            Log.d("DEBUG foundCount", "Fragment observe -> Всего вакансий найдено: $foundCount")
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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (text.isNotEmpty()) {
                        viewModel.cancelSearchDebounce()
                        viewModel.startSearch(text.toString())
                    }
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
    }

    private fun renderScreen(state: SearchScreenState) {
        when (state) {
            is Default -> TODO()
            is Loading -> showProgressBar()
            is Error -> placeholderManager(state)
            is SearchResults -> showSearchResults(state.pagingData)

        }
        Log.d("DEBUG", "SearchScreenState = $state")
    }

    // обработка разных типов ошибок
    private fun placeholderManager(state: Error) {
        when (state) {
            is NetworkError -> TODO()
            is NothingFound -> TODO()
            is ServerError -> TODO()
        }
    }

    // отображение сообщения "Найдено $count вакансий"
    private fun showFoundCount(count: Int) {
        //TODO
    }

    private fun hideProgressBar(state: SearchScreenState) {
        if (state != Loading) {
            //TODO
        }
    }

    private fun showProgressBar() {
        //TODO
    }

    private fun showSearchResults(pagingData: PagingData<Vacancy>) {
        binding.recycler.isVisible = true
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
