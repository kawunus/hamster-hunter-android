package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.ui.FilterUiHelper
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel

val uiModule = module {
    factory { (binding: FragmentFilterBinding, viewModel: FilterViewModel) ->
        FilterUiHelper(binding, viewModel, get())
    }
}
