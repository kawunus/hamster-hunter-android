package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.ui.FilterUiHelper
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_BUNDLE_KEY
import ru.practicum.android.diploma.util.Constants.FILTERS_CHANGED_REQUEST_KEY

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel()
    private val uiHelper: FilterUiHelper by inject { parametersOf(binding, viewModel) }

    override fun initViews() {
        setClickListeners()
        uiHelper.setupSalaryTextWatcher()
        handleClearButtonClick()
        viewModel.checkSavedFilters()
        setupTextWatchers()
    }

    override fun subscribe() {
        with(viewModel) {
            getSavedFilters().observe(viewLifecycleOwner) { uiHelper.renderScreen(it) }
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

    private fun setClickListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
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

    private fun handleClearButtonClick() {
        binding.btnClear.setOnClickListener {
            binding.tetSalary.text?.clear()
        }
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

    private fun isDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}
