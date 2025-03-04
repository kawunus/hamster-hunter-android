import android.content.res.ColorStateList
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.model.FilterParameters
import ru.practicum.android.diploma.filter.presentation.ui.fragment.FilterFragmentDirections
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.formatLocationString
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class FilterUiHelper(private val binding: FragmentFilterBinding, private val viewModel: FilterViewModel) {

    private var isTextUpdating = false

    fun setupSalaryTextWatcher() {
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
            ContextCompat.getColor(binding.root.context, R.color.blue),
            ContextCompat.getColor(binding.root.context, R.color.focus_tint)
        )
        val valuedColor = intArrayOf(
            ContextCompat.getColor(binding.root.context, R.color.blue),
            ContextCompat.getColor(binding.root.context, R.color.black)
        )
        val hintColorList = ColorStateList(hintStates, emptyColor)
        val hintColorValuedList = ColorStateList(hintStates, valuedColor)
        if (text.isNullOrEmpty()) {
            binding.tilSalary.defaultHintTextColor = hintColorList
        } else {
            binding.tilSalary.defaultHintTextColor = hintColorValuedList
        }
    }

    private fun handleSalaryText(text: CharSequence?) {
        clearButtonVisibilityManager(text)
        if (text.isNullOrEmpty()) {
            viewModel.setSalary(null)
            return
        }

        val salaryText = text.toString()
        val salary = salaryText.toIntOrNull()

        if (salary != null && salary > 0) {
            viewModel.setSalary(salary)
        } else {
            Toast.makeText(
                binding.root.context,
                binding.root.context.getString(R.string.error_too_big_salary),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearButtonVisibilityManager(text: CharSequence?) {
        if (text?.isNotEmpty() == true) {
            binding.btnClear.show()
        } else {
            binding.btnClear.hide()
        }
    }

    fun renderScreen(filterParameters: FilterParameters) {
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
        binding.tetSalary.apply {
            if (text.toString() != salaryText) {
                isTextUpdating = true
                setText(salaryText)
                isTextUpdating = false
            }
        }
        clearButtonVisibilityManager(salaryText)
    }

    private fun updateAreaIcon(areaName: String?) {
        binding.tilArea.apply {
            if (!areaName.isNullOrEmpty()) {
                endIconDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_close)
                setEndIconOnClickListener {
                    viewModel.setArea(null)
                }
            } else {
                endIconDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_arrow_forward)
                setEndIconOnClickListener {
                    findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToAreaFragment())
                }
            }
        }
    }

    private fun updateIndustryIcon(industryName: String?) {
        binding.tilIndustry.apply {
            if (!industryName.isNullOrEmpty()) {
                endIconDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_close)
                setEndIconOnClickListener {
                    viewModel.setIndustry(null)
                }
            } else {
                endIconDrawable = AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_arrow_forward)
                setEndIconOnClickListener {
                    findNavController().navigate(FilterFragmentDirections.actionFilterFragmentToIndustryFragment())
                }
            }
        }
    }
}
