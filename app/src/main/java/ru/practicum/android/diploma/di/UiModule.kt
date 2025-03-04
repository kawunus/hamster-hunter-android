package ru.practicum.android.diploma.di

import FilterUiHelper
import org.koin.dsl.module
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel

val uiModule = module {
    factory { (binding: FragmentFilterBinding, viewModel: FilterViewModel) ->
        FilterUiHelper(binding, viewModel)
    }
}
