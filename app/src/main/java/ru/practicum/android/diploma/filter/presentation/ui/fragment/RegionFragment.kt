package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.presentation.view_model.RegionViewModel

class RegionFragment(
    override val viewModel: RegionViewModel
) : BaseFragment<FragmentRegionBinding, RegionViewModel>(
    inflate = FragmentRegionBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
