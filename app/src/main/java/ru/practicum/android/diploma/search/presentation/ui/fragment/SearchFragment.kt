package ru.practicum.android.diploma.search.presentation.ui.fragment

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModels()

    override fun initViews() {

        // TODO: init views
    }

    override fun subscribe() {

        // TODO: subscribe on viewModel
    }
}
