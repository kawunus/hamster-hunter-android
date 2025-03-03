package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_BUNDLE_KEY
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_REQUEST_KEY
import ru.practicum.android.diploma.util.formatLocationString
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel()
    private var isTextUpdating = false

    override fun initViews() {
        setClickListeners()
        setupSalaryTextWatcher()
        handleClearButtonClick()
        viewModel.checkSavedFilters()
        setupTextWatchers()
    }

    override fun subscribe() {
        with(viewModel) {
            getSavedFilters().observe(viewLifecycleOwner) { renderScreen(it) }
            getFilterWasChanged().observe(viewLifecycleOwner) {
                setApplyBtnVisibility(it)
            }
            getAnyFilterApplied().observe(viewLifecycleOwner) {
                setResetBtnVisibility(it)
            }
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
                    .navigate(FilterFragmentDirections.actionFilterFragmentToAreaFragment())
            }
            tetIndustry.setOnClickListener {
                findNavController()
                    .navigate(FilterFragmentDirections.actionFilterFragmentToIndustryFragment())
            }

            checkBoxSalary.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyWithSalary(isChecked)
            }

            checkBoxSearchInTitle.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyInTitles(isChecked)
            }

            btnApply.setOnClickListener {
                // по нажатию применить, ставим флаг, что фильтры изменились
                setFragmentResult(FILTERS_CHANGED_REQUEST_KEY, bundleOf(FILTERS_CHANGED_BUNDLE_KEY to true))
                findNavController()
                    .navigateUp()
            }

            btnReset.setOnClickListener { viewModel.clearFilters() }
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSalaryTextWatcher() {
        binding.tetSalary.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateSalaryHintColor(text)
                if (!isTextUpdating) {
                    handleSalaryText(text)
                }
            }
        )
    }

    private fun updateSalaryHintColor(text: CharSequence?) {
        val hintStates = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf()
        )
        val emptyColor = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.blue),
            ContextCompat.getColor(requireContext(), R.color.focus_tint)
        )
        val valuedColor = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.blue),
            ContextCompat.getColor(requireContext(), R.color.black)
        )
        val hintColorList = ColorStateList(hintStates, emptyColor)
        val hintColorValuedList = ColorStateList(hintStates, valuedColor)
        if (text.isNullOrEmpty()) {
            binding.tilSalary.defaultHintTextColor = hintColorList
        } else {
            binding.tilSalary.defaultHintTextColor = hintColorValuedList
        }
    }

    // обработка нажатия на кнопку очистки
    private fun handleClearButtonClick() {
        binding.btnClear.setOnClickListener {
            binding.tetSalary.text?.clear()
        }
    }

    private fun handleSalaryText(text: CharSequence?) {
        clearButtonVisibilityManager(text)
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

    }

    // отображение иконки очистки
    private fun clearButtonVisibilityManager(text: CharSequence?) {
        if (text?.isNotEmpty() == true) {
            binding.btnClear.show()
        } else {
            binding.btnClear.hide()
        }
    }

    private fun renderScreen(filterParameters: FilterParameters) {
        with(filterParameters) {
            renderAreaFilter(formatLocationString(area))
            renderIndustryFilter(industry?.name)
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
        clearButtonVisibilityManager(salaryText)
//        updateSalaryHintColor()

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
        binding.btnReset.isVisible = anyFilterApplied ?: false
    }

    private fun setupTextWatchers() {
        binding.tetArea.addTextChangedListener(createTextWatcher(binding.tilArea))
        binding.tetIndustry.addTextChangedListener(createTextWatcher(binding.tilIndustry))
    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                updateHintTextColor(textInputLayout, s)
            }
        }
    }

    private fun updateHintTextColor(textInputLayout: TextInputLayout, text: Editable?) {
        with(textInputLayout) {
            defaultHintTextColor = ColorStateList.valueOf(
                resources.getColor(
                    if (text.isNullOrBlank()) {
                        R.color.gray
                    } else {
                        if (isDarkTheme()) R.color.white else R.color.black
                    },
                    null
                )
            )
        }
    }

    // Проверка темы (светлая/темная)
    private fun isDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}
