package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.text.Editable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentAreaBinding
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.presentation.viewmodel.AreaViewModel
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class AreaFragment : BaseFragment<FragmentAreaBinding, AreaViewModel>(FragmentAreaBinding::inflate) {

    override val viewModel: AreaViewModel by viewModel()

    override fun initViews() {
        binding.btnBack.setOnClickListener {
            viewModel.clearTempFilters()
            findNavController().navigateUp()
        }

        binding.tetCountry.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_countryFragment)
        }

        binding.tetRegion.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_regionFragment)
        }

        binding.tilCountry.setEndIconOnClickListener {
            if (!binding.tetCountry.text.isNullOrEmpty()) {
                viewModel.updateArea(Area(country = null, region = null)) // полностью сбрасываем страну
            }
        }

        binding.tilRegion.setEndIconOnClickListener {
            if (!binding.tetRegion.text.isNullOrEmpty()) {
                val currentArea = viewModel.selectedArea.value
                viewModel.updateArea(
                    currentArea!!.copy(country = currentArea.country, region = null)
                )
            }
        }

        binding.btnSelect.setOnClickListener {
            viewModel.saveFilters()
            findNavController().navigateUp()
        }

        // TextWatcher для tetCountry и tetRegion
        setTextChangedListeners()
    }

    override fun subscribe() {
        viewModel.getActualFilterState()
        viewModel.selectedArea.observe(viewLifecycleOwner) { area ->
            val countryName = area?.country?.name.orEmpty()
            val regionName = area?.region?.name.orEmpty()

            updateFieldsAndButtonVisibility(countryName, regionName)

            updateCountryIcon(countryName)

            updateRegionIcon(regionName)

            if (regionName.isNotEmpty() && countryName.isEmpty()) {
                viewModel.getCountryByRegion()
            }
        }

        viewModel.isAcceptable.observe(viewLifecycleOwner) { isAcceptable ->
            if (!isAcceptable) {
                binding.btnSelect.show()
            } else {
                binding.btnSelect.hide()
            }
        }
    }

    private fun updateFieldsAndButtonVisibility(countryName: String, regionName: String) {
        binding.tetCountry.setText(countryName)
        binding.tetRegion.setText(regionName)
        viewModel.areFiltersEqual(countryName, regionName)
    }

    private fun updateCountryIcon(countryName: String) {
        if (countryName.isNotEmpty()) {
            binding.tilCountry.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_close)
            binding.tilCountry.setEndIconOnClickListener {
                viewModel.updateArea(Area(country = null, region = null))
            }
        } else {
            binding.tilCountry.endIconDrawable =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_forward)
            binding.tilCountry.setEndIconOnClickListener {
                findNavController().navigate(R.id.action_areaFragment_to_countryFragment)
            }
        }
    }

    private fun updateRegionIcon(regionName: String) {
        if (regionName.isNotEmpty()) {
            binding.tilRegion.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_close)
            binding.tilRegion.setEndIconOnClickListener {
                val currentArea = viewModel.selectedArea.value
                viewModel.updateArea(currentArea?.copy(region = null) ?: Area(null, null))
            }
        } else {
            binding.tilRegion.endIconDrawable =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_arrow_forward)
            binding.tilRegion.setEndIconOnClickListener {
                findNavController().navigate(R.id.action_areaFragment_to_regionFragment)
            }
        }
    }

    private fun setTextChangedListeners() {
        binding.tetCountry.addTextChangedListener(afterTextChanged = { s ->
            updateHintTextColor(binding.tilCountry, s)
        })
        binding.tetRegion.addTextChangedListener(afterTextChanged = { s ->
            updateHintTextColor(binding.tilRegion, s)
        })
    }

    private fun updateHintTextColor(textInputLayout: TextInputLayout, text: Editable?) {
        with(textInputLayout) {
            defaultHintTextColor = ColorStateList.valueOf(
                resources.getColor(
                    if (text.isNullOrBlank()) {
                        R.color.gray
                    } else {
                        if (isDarkTheme()) R.color.white else R.color.black
                    }, null
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
