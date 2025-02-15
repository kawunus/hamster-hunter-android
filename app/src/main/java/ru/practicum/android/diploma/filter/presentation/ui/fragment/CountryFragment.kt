package ru.practicum.android.diploma.filter.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.CountryViewModel

class CountryFragment(
    override val viewModel: CountryViewModel
) : BaseFragment<FragmentCountryBinding, CountryViewModel>(
    inflate = FragmentCountryBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
