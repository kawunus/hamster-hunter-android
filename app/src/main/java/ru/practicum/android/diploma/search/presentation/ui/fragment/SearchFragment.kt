package ru.practicum.android.diploma.search.presentation.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModels()

    override fun initViews() {
        setupSearchTextWatcher()
        setupClearButtonClickListener()
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateClearButtonIcon(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
        }
    }

    // обработка нажатия на кнопку очистки
    private fun handleClearButtonClick() {
        if (binding.edittextSearch.text.isNotEmpty()) {
            binding.edittextSearch.text.clear()
        }
    }

    override fun subscribe() {
    }
}
