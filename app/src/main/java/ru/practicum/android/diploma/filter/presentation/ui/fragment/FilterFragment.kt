package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.search.presentation.ui.fragment.SearchFragmentDirections

class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(
    inflate = FragmentFilterBinding::inflate
) {
    override val viewModel: FilterViewModel by viewModel<FilterViewModel>()

    override fun initViews() {
        setupButtonClickListener()
    }

    override fun subscribe() {
        // Реализация subscribe
    }

    // настройка слушателя нажатий
    private fun setupButtonClickListener() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
