package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentAreaBinding
import ru.practicum.android.diploma.filter.domain.model.Area
import ru.practicum.android.diploma.filter.presentation.viewmodel.AreaViewModel

class AreaFragment : BaseFragment<FragmentAreaBinding, AreaViewModel>(FragmentAreaBinding::inflate) {

    override val viewModel: AreaViewModel by viewModel()

    override fun initViews() {
        binding.tetJobLocation.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_countryFragment)
        }

        binding.tilJobLocation.setEndIconOnClickListener {
            if (!binding.tetJobLocation.text.isNullOrEmpty()) {
                viewModel.updateArea(Area()) // полностью сбрасываем страну
            } else {
            }
        }

        binding.tetIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_regionFragment)
        }

        binding.tilIndustry.setEndIconOnClickListener {
            if (!binding.tetIndustry.text.isNullOrEmpty()) {
                val currentArea = viewModel.selectedArea.value
                viewModel.updateArea(
                    currentArea?.copy(regionId = null, regionName = null) ?: Area()
                )
            }
        }

        binding.btnSelect.setOnClickListener {
            viewModel.saveFilters()
            findNavController().navigateUp()
        }
    }

    override fun subscribe() {
        viewModel.selectedArea.observe(viewLifecycleOwner) { area ->
            binding.tetJobLocation.setText(area?.countryName ?: "")
            binding.tetIndustry.setText(area?.regionName ?: "")
        }
    }
}
