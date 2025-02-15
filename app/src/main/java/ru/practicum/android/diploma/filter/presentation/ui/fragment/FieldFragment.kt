package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFieldBinding
import ru.practicum.android.diploma.filter.presentation.view_model.FieldViewModel

class FieldFragment(
    override val viewModel: FieldViewModel
) : BaseFragment<FragmentFieldBinding, FieldViewModel>(
    inflate = FragmentFieldBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
