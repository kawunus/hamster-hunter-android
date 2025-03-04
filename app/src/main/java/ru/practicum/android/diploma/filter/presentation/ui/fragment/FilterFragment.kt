package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
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
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_BUNDLE_KEY
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_REQUEST_KEY
import ru.practicum.android.diploma.util.formatLocationString

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel()
    private var isTextUpdating = false

    override fun initViews() {
        setClickListeners()
        handleClearButtonClick()
        viewModel.checkSavedFilters()
        setupTextWatchers()
    }

    override fun subscribe() = with(viewModel) {
        getSavedFilters().observe(viewLifecycleOwner) { renderScreen(it) }
        getFilterWasChanged().observe(viewLifecycleOwner) { setApplyBtnVisibility(it) }
        getAnyFilterApplied().observe(viewLifecycleOwner) { setResetBtnVisibility(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSavedFilters()
    }

    private fun setClickListeners() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().navigateUp() }
            tetArea.setOnClickListener {
                findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToAreaFragment())
            }
            tetIndustry.setOnClickListener {
                findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToIndustryFragment())
            }
            checkBoxSalary.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyWithSalary(isChecked)
            }
            checkBoxSearchInTitle.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyInTitles(isChecked)
            }
            btnApply.setOnClickListener {
                setFragmentResult(FILTERS_CHANGED_REQUEST_KEY, bundleOf(FILTERS_CHANGED_BUNDLE_KEY to true))
                findNavController().navigateUp()
            }
            btnReset.setOnClickListener { viewModel.clearFilters() }
        }
    }

    private fun updateSalaryHintColor(text: CharSequence?) {
        val (emptyColor, valuedColor) = arrayOf(
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.blue),
                ContextCompat.getColor(requireContext(), R.color.focus_tint)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.blue),
                ContextCompat.getColor(requireContext(), R.color.black)
            )
        )
        binding.tilSalary.defaultHintTextColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
            if (text.isNullOrEmpty()) emptyColor else valuedColor
        )
    }

    private fun handleClearButtonClick() {
        binding.btnClear.setOnClickListener { binding.tetSalary.text?.clear() }
    }

    private fun handleSalaryText(text: CharSequence?) {
        clearButtonVisibilityManager(text)
        if (text.isNullOrEmpty()) {
            viewModel.setSalary(null)
            return
        }
        val salary = text.toString().toIntOrNull()
        if (salary != null && salary > 0) {
            viewModel.setSalary(salary)
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_too_big_salary), Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearButtonVisibilityManager(text: CharSequence?) {
        binding.btnClear.isVisible = !text.isNullOrEmpty()
    }

    private fun renderScreen(filterParameters: FilterParameters) {
        binding.apply {
            with(filterParameters) {
                renderAreaFilter(formatLocationString(area))
                renderIndustryFilter(industry?.name)
                renderSalaryFilter(salary)
                checkBoxSalary.isChecked = onlyWithSalary ?: false
                checkBoxSearchInTitle.isChecked = onlyInTitles ?: false
            }
        }
    }

    private fun renderAreaFilter(areaName: String?) {
        binding.tetArea.setText(areaName)
        updateAreaIcon(areaName)
    }

    private fun renderIndustryFilter(industryName: String?) {
        binding.tetIndustry.setText(industryName)
        updateIndustryIcon(industryName)
    }

    private fun renderSalaryFilter(salary: Int?) {
        val salaryText = salary?.toString() ?: Constants.EMPTY_STRING
        if (binding.tetSalary.text.toString() != salaryText) {
            isTextUpdating = true
            binding.tetSalary.setText(salaryText)
            isTextUpdating = false
        }
        clearButtonVisibilityManager(salaryText)
    }

    private fun setApplyBtnVisibility(filterWasChanged: Boolean) {
        binding.btnApply.isVisible = filterWasChanged
    }

    private fun setResetBtnVisibility(anyFilterApplied: Boolean?) {
        binding.btnReset.isVisible = anyFilterApplied ?: false
    }

    private fun setupTextWatchers() {
        binding.apply {
            tetArea.addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                    updateHintTextColor(binding.tilArea, text)
                }
            )
            tetIndustry.addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                    updateHintTextColor(binding.tilIndustry, text)
                }
            )
            tetSalary.addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                    updateSalaryHintColor(text)
                    if (!isTextUpdating) {
                        handleSalaryText(text)
                    }
                }
            )
        }
    }

    private fun updateHintTextColor(textInputLayout: TextInputLayout, text: CharSequence?) = with(textInputLayout) {
        defaultHintTextColor = ColorStateList.valueOf(
            resources.getColor(
                if (text.isNullOrBlank()) {
                    R.color.gray
                } else if (isDarkTheme()) {
                    R.color.white
                } else {
                    R.color.black
                },
                null
            )
        )
    }

    private fun updateAreaIcon(areaName: String?) {
        updateIcon(
            areaName.isNullOrEmpty(),
            binding.tilArea,
            { viewModel.setArea(null) },
            { findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToAreaFragment()) }
        )
    }

    private fun updateIndustryIcon(industryName: String?) {
        updateIcon(
            industryName.isNullOrEmpty(),
            binding.tilIndustry,
            { viewModel.setIndustry(null) },
            { findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToIndustryFragment()) }
        )
    }

    private fun updateIcon(
        isEmpty: Boolean,
        layout: TextInputLayout,
        onClear: () -> Unit,
        onNavigate: () -> Unit
    ) = with(layout) {
        endIconDrawable = AppCompatResources.getDrawable(
            requireContext(),
            if (isEmpty) {
                R.drawable.ic_arrow_forward
            } else {
                R.drawable.ic_close
            }
        )
        setEndIconOnClickListener { if (isEmpty) onNavigate() else onClear() }
    }

    private fun isDarkTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES
    }
}
