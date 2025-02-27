package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FieldViewModel

class IndustryFragment : BaseFragment<FragmentIndustryBinding, FieldViewModel>(
    inflate = FragmentIndustryBinding::inflate
) {
    override val viewModel: FieldViewModel by viewModel()
    override fun initViews() {
        // инициализируем наши вьюхи тут
    }

    override fun subscribe() {
        // подписка на данные от viewModel
    }
}
