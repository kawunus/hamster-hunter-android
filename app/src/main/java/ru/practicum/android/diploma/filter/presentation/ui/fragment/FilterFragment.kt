package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel

class FilterFragment(
    override val viewModel: FilterViewModel
) : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        viewModel.getSavedFiltersLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSavedFilters()
    }
}
