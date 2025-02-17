package ru.practicum.android.diploma.favorites.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavoritesViewModel

class FavoritesFragment(
    override val viewModel: FavoritesViewModel
) : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    inflate = FragmentFavoritesBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
