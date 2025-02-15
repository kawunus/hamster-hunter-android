package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.view_model.FieldViewModel

class FilterFragment(
    override val viewModel: FieldViewModel
) : BaseFragment<FragmentFilterBinding, FieldViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
