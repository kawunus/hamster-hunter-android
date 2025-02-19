package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.search.presentation.ui.fragment.SearchFragment.Companion.PlaceholderStatus.EMPTY
import ru.practicum.android.diploma.search.presentation.ui.fragment.SearchFragment.Companion.PlaceholderStatus.HIDDEN
import ru.practicum.android.diploma.search.presentation.ui.fragment.SearchFragment.Companion.PlaceholderStatus.NOTHING_FOUND
import ru.practicum.android.diploma.search.presentation.ui.fragment.SearchFragment.Companion.PlaceholderStatus.NO_NETWORK
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchScreenState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModels()

    private val adapter = VacancyAdapter { vacancy ->
        if (clickDebounce()) {
            // Обрабаботка клика по вакансии должна быть ТУТ
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

    // добавила инициализацию поиска по клику на кнопку на клавиатуре, чтобы не заставлять юзера ждать 2 сек
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
        binding.recycler.adapter = adapter
    }

    private fun renderScreen(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Empty -> placeholderManager(EMPTY)
            is SearchScreenState.Loading -> showProgressBar()
            is SearchScreenState.NetworkError -> placeholderManager(NO_NETWORK)
            is SearchScreenState.NothingFound -> placeholderManager(NOTHING_FOUND)
            is SearchScreenState.SearchResults -> showSearchResults()

        }
    }

    private fun hideProgressBar(state: SearchScreenState) {
        if (state != SearchScreenState.Loading) {
            //TODO
        }
    }

    private fun showProgressBar() {
        //TODO
        placeholderManager(HIDDEN)
    }

    private fun placeholderManager(status: PlaceholderStatus) {
        when (status) {
            EMPTY -> binding.placeholderMain.isVisible = true
            NOTHING_FOUND -> TODO()
            NO_NETWORK -> {
                with(binding) {
                    networkErrorImage.isVisible = true
                    errorText.isVisible = true
                }
            }

            HIDDEN -> with(binding) {
                networkErrorImage.isVisible = false
                errorText.isVisible = false
            }
        }
    }

    private fun showSearchResults() {
        binding.recycler.isVisible = true
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

        enum class PlaceholderStatus {
            EMPTY,
            NOTHING_FOUND,
            NO_NETWORK,
            HIDDEN
        }
    }
}
