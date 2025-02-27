package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.RegionViewModel

class RegionFragment : BaseFragment<FragmentRegionBinding, RegionViewModel>(
    inflate = FragmentRegionBinding::inflate
) {
    override val viewModel: RegionViewModel by viewModel()
    override fun initViews() {
        // инициализируем наши вьюхи тут
    }

    override fun subscribe() {
        // подписка на данные от viewModel
    }
}
