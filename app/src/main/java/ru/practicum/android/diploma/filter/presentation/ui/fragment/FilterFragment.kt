package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel()

    override fun initViews() {
        setupButtonClickListener()
        setupSearchTextWatcher()
        handleClearButtonClick()
    }

    override fun subscribe() {
        // Реализация subscribe
    }

    // настройка слушателя нажатий
    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.tetSalary.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                visibleClearButtonIcon(text)
            }
        )
    }

    // обработка нажатия на кнопку очистки
    private fun handleClearButtonClick() {
        binding.btnClear.setOnClickListener {
            binding.tetSalary.text?.clear()
        }
    }

    // отображение иконки очистки
    private fun visibleClearButtonIcon(text: CharSequence?) {
        if (text?.isNotEmpty() == true) {
            binding.btnClear.show()
        } else {
            binding.btnClear.hide()
        }
    }
}
