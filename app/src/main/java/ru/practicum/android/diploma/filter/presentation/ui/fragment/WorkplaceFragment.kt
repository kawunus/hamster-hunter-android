package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentWorkplaceBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.WorkplaceViewModel

class WorkplaceFragment(
    override val viewModel: WorkplaceViewModel
) : BaseFragment<FragmentWorkplaceBinding, WorkplaceViewModel>(
    inflate = FragmentWorkplaceBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
