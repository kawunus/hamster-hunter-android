package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentWorkplaceBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.WorkplaceViewModel

class WorkplaceFragment : BaseFragment<FragmentWorkplaceBinding, WorkplaceViewModel>(
    inflate = FragmentWorkplaceBinding::inflate
) {
    override val viewModel: WorkplaceViewModel by viewModel()
    override fun initViews() {

    }

    override fun subscribe() {

    }
}
