package ru.practicum.android.diploma.filter.presentation.ui.fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.CountryViewModel

class CountryFragment : BaseFragment<FragmentCountryBinding, CountryViewModel>(
    inflate = FragmentCountryBinding::inflate
) {
    override val viewModel: CountryViewModel by viewModel()
    override fun initViews() {
        // инициализируем наши вьюхи тут
    }

    override fun subscribe() {
        // подписка на данные от viewModel
    }
}
