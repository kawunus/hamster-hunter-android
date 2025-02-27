package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel()
    private var isTextUpdating = false
    private val args by navArgs<FilterFragmentArgs>()

    override fun initViews() {
        setClickListeners()
        setupSalaryTextWatcher()
        handleClearButtonClick()
        viewModel.checkSavedFilters()
    }

    override fun subscribe() {
        with(viewModel) {
            getSavedFilters().observe(viewLifecycleOwner) { renderScreen(it) }
            getFilterWasChanged().observe(viewLifecycleOwner) { setApplyBtnVisibility(it) }
            getAnyFilterApplied().observe(viewLifecycleOwner) { setResetBtnVisibility(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSavedFilters()
    }

    // настройка слушателя нажатий
    private fun setClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController()
                    .navigateUp()
            }
            tetArea.setOnClickListener {
                findNavController()
                    .navigate(FilterFragmentDirections.actionFilterFragmentToWorkplaceFragment())
            }
            tetIndustry.setOnClickListener {
                findNavController()
                    .navigate(FilterFragmentDirections.actionFilterFragmentToFieldFragment())
            }

            checkBoxSalary.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyWithSalary(isChecked)
            }

            checkBoxSearchInTitle.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyInTitles(isChecked)
            }

            btnApply.setOnClickListener {
                val action = FilterFragmentDirections.actionFilterFragmentToSearchFragment(args.searchQuery)
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.filterFragment, true) // Очищает стек до FilterFragment (включительно)
                    .build()
                findNavController().navigate(action, navOptions)
            }

            btnReset.setOnClickListener { viewModel.clearFilters() }
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSalaryTextWatcher() {
        binding.tetSalary.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                if (!isTextUpdating) {
                    handleSalaryText(text)
                }
            }
        )
    }

    // обработка нажатия на кнопку очистки
    private fun handleClearButtonClick() {
        binding.btnClear.setOnClickListener {
            binding.tetSalary.text?.clear()
        }
    }

    private fun handleSalaryText(text: CharSequence?) {
        // Проверяем, что текст не null и не пустой
        if (text.isNullOrEmpty()) {
            viewModel.setSalary(null)
            return
        }

        val salaryText = text.toString()
        val salary = salaryText.toIntOrNull() // если salary превысит Int.MAX_VALUE, вернётся null

        if (salary != null && salary >= 0) {
            viewModel.setSalary(salary)
        } else {
            // Обработка случая, когда число превышает Int.MAX_VALUE или не является числом
            Toast.makeText(requireContext(), "Введите число от 0 до 2 147 483 647", Toast.LENGTH_SHORT).show()
        }

        visibleClearButtonIcon(text)
    }

    // отображение иконки очистки
    private fun visibleClearButtonIcon(text: CharSequence?) {
        if (text?.isNotEmpty() == true) {
            binding.btnClear.show()
        } else {
            binding.btnClear.hide()
        }
    }

    private fun renderScreen(filterParameters: FilterParameters) {
        with(filterParameters) {
            renderAreaFilter(area)
            renderIndustryFilter(professionalRole)
            renderSalaryFilter(salary)
            renderOnlyWithSalaryFilter(onlyWithSalary)
            renderOnlyInTitlesFilter(onlyInTitles)
        }
    }

    private fun renderAreaFilter(area: String?) {
        binding.tetArea.setText(area)
    }

    private fun renderIndustryFilter(industry: String?) {
        binding.tetIndustry.setText(industry)
    }

    private fun renderSalaryFilter(salary: Int?) {
        val salaryText = salary?.toString() ?: ""
        binding.tetSalary.apply {
            if (text.toString() != salaryText) {
                isTextUpdating = true
                setText(salaryText)
                isTextUpdating = false
            }
        }
    }

    private fun renderOnlyWithSalaryFilter(onlyWithSalary: Boolean?) {
        binding.checkBoxSalary.isChecked = onlyWithSalary ?: false
    }

    private fun renderOnlyInTitlesFilter(onlyInTitles: Boolean?) {
        binding.checkBoxSearchInTitle.isChecked = onlyInTitles ?: false
    }

    private fun setApplyBtnVisibility(filterWasChanged: Boolean) {
        binding.btnApply.isVisible = filterWasChanged
    }

    private fun setResetBtnVisibility(anyFilterApplied: Boolean?) {
        binding.btnApply.isVisible = anyFilterApplied ?: false
    }
}
