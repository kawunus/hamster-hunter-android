package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentAreaBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.WorkplaceViewModel

class AreaFragment : BaseFragment<FragmentAreaBinding, WorkplaceViewModel>(
    inflate = FragmentAreaBinding::inflate
) {
    override val viewModel: WorkplaceViewModel by viewModel()
    override fun initViews() {
        // инициализируем наши вьюхи тут
    }

    override fun subscribe() {
        // подписка на данные от viewModel
    }
}
