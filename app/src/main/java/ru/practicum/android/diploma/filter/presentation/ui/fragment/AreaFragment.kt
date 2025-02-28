package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentAreaBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.AreaViewModel

class AreaFragment : BaseFragment<FragmentAreaBinding, AreaViewModel>(FragmentAreaBinding::inflate) {

    override val viewModel: AreaViewModel by viewModels()

    override fun initViews() {
        binding.tetJobLocation.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_countryFragment)
        }

        binding.tetIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_areaFragment_to_regionFragment)
        }

        binding.btnSelect.setOnClickListener {
            viewModel.saveFilters()
            findNavController().popBackStack()
        }
    }

    override fun subscribe() {
        viewModel.selectedArea.observe(viewLifecycleOwner) { area ->
            binding.tetJobLocation.setText(area?.name ?: getString(R.string.choose_country))
        }

        viewModel.selectedRegion.observe(viewLifecycleOwner) { region ->
            binding.tetIndustry.setText(region ?: getString(R.string.choose_region))
        }
    }
}
