package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentAreaBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.WorkplaceViewModel

class AreaFragment : BaseFragment<FragmentAreaBinding, WorkplaceViewModel>(
    inflate = FragmentAreaBinding::inflate
) {
    override val viewModel: WorkplaceViewModel by viewModel()

    override fun initViews() {
        with(binding) {
            tetIndustry.setOnClickListener {
                viewModel.selectedArea.value.countryId?.let { countryId ->
                    findNavController().navigate(
                        AreaFragmentDirections.actionAreaFragmentToRegionFragment(countryId = countryId)
                    )
                } ?: run {
                    // Временно используем дефолтное значение, пока нет выбора страны
                    findNavController().navigate(
                        AreaFragmentDirections.actionAreaFragmentToRegionFragment(countryId = "113")
                    )
                }
            }
        }
    }

    override fun subscribe() {
        setFragmentResultListener(RegionFragment.REQUEST_KEY_REGION) { _, bundle ->
            val regionId = bundle.getString(RegionFragment.BUNDLE_KEY_REGION_ID)
            val regionName = bundle.getString(RegionFragment.BUNDLE_KEY_REGION_NAME)

            binding.tetIndustry.setText(regionName)

            regionId?.let { id ->
                regionName?.let { name ->
                    viewModel.setRegion(id, name)
                }
            }
        }
    }
}
