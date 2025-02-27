package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFieldBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FieldViewModel

class FieldFragment : BaseFragment<FragmentFieldBinding, FieldViewModel>(
    inflate = FragmentFieldBinding::inflate
) {
    override val viewModel: FieldViewModel by viewModel()
    override fun initViews() {

    }

    override fun subscribe() {

    }
}
