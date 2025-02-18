package ru.practicum.android.diploma.favorites.presentation.ui.fragment

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavoritesViewModel

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    inflate = FragmentFavoritesBinding::inflate
) {
    override val viewModel: FavoritesViewModel by viewModels()

    override fun initViews() {

        // TODO: init views
    }

    override fun subscribe() {

        // TODO: subscribe on viewModel
    }
}
