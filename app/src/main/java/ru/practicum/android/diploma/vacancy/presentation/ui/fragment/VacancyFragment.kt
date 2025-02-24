package ru.practicum.android.diploma.vacancy.presentation.ui.fragment

import android.util.Log
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(
    inflate = FragmentVacancyBinding::inflate
) {
    override val viewModel: VacancyViewModel by viewModel()
    private val args by navArgs<VacancyFragmentArgs>()
    private val vacancyId by lazy { args.vacancyId }

    override fun initViews() {
        viewModel.getVacancy(vacancyId.toInt())
    }

    override fun subscribe() {
        viewModel.observeVacancy.observe(viewLifecycleOwner) {
            Log.d("VacancyFragment", "Vacancy: $it")
        }
    }
}
