package ru.practicum.android.diploma.vacancy.presentation.ui.fragment

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.util.formatSalary
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsState
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(
    inflate = FragmentVacancyBinding::inflate
) {
    override val viewModel: VacancyViewModel by viewModel {
        val args by navArgs<VacancyFragmentArgs>()
        parametersOf(args)
    }

    override fun initViews() {
        bindButtons()
    }

    override fun subscribe() = with(binding) {
        viewModel.observeVacancyDetailsState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyDetailsState.Loading -> showLoading()

                is VacancyDetailsState.NotFoundError -> showErrorNotFound()

                is VacancyDetailsState.ServerError -> showErrorServer()

                is VacancyDetailsState.VacancyLiked -> showVacancyDetails(state)

                else -> {}
            }
        }
    }

    private fun bindButtons() = with(binding) {
        buttonBack.setOnClickListener { findNavController().navigateUp() }
        buttonShare.setOnClickListener { viewModel.shareButtonControll() }
        buttonLike.setOnClickListener { viewModel.likeButtonControl() }
    }

    private fun renderVacancyInfo(vacancyDetails: VacancyDetails) {
        binding.name.text = getString(
            R.string.vacancy_name_and_location, vacancyDetails.name, vacancyDetails.id
        )
        binding.salary.text =
            formatSalary(vacancyDetails.salaryFrom, vacancyDetails.salaryTo, vacancyDetails.currency, requireContext())
        binding.employerName.text = vacancyDetails.company
        val displayArea = if (vacancyDetails.address?.trim().isNullOrEmpty()) {
            vacancyDetails.area
        } else {
            vacancyDetails.address
        }
        binding.employerLocation.text = displayArea
        binding.experience.text = vacancyDetails.experience
        binding.employmentFormAndWorkFormat.text = getString(
            R.string.vacancy_name_and_location, vacancyDetails.employmentForm, vacancyDetails.workFormat
        )
        // !!!!!!!!!!!!!!!----не забыть дополнить по выполнению коллегами таска 47------!!!!!!!!!!!!
        binding.jobDescription.text = vacancyDetails.description
        // загружаю ключевые скиллы
        var keySkills = ""
        for (i in vacancyDetails.keySkills) {
            keySkills += getString(R.string.key_skill_separator, i)
        }
        binding.keySkills.text = keySkills
        // загружаю иконку
        Glide.with(this.requireContext()).load(vacancyDetails.icon).placeholder(R.drawable.placeholder_32px).fitCenter()
            .into(binding.employerImg)
    }

    private fun renderError(showErrorOrNot: Boolean) {
        binding.errorBox.isVisible = showErrorOrNot
        binding.jobInfo.isVisible = !showErrorOrNot
    }

    private fun changeErrorMessage(isServerError: Boolean) {
        if (isServerError) {
            binding.errorImg.setImageResource(R.drawable.placeholder_server_vacancy_error)
            binding.errorText.text = getString(R.string.error_server)
        } else {
            binding.errorImg.setImageResource(R.drawable.placeholder_job_deleted_error)
            binding.errorText.text = getString(R.string.error_job_not_found_or_deleted)
        }
    }

    private fun showErrorServer() {
        renderError(true)
        binding.progressBar.isVisible = false
        changeErrorMessage(true)
    }

    private fun showErrorNotFound() {
        renderError(true)
        binding.progressBar.isVisible = false
        changeErrorMessage(false)
    }

    private fun showLoading() {
        renderError(false)
        binding.jobInfo.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showVacancyDetails(vacancyDetailsState: VacancyDetailsState.VacancyLiked) {
        renderError(false)
        binding.progressBar.isVisible = false
        renderVacancyInfo(vacancyDetailsState.details)
        val iconRes = if (vacancyDetailsState.isLiked) {
            R.drawable.ic_favorites_on
        } else {
            R.drawable.ic_favorites_off
        }
        binding.buttonLike.setImageResource(iconRes)
    }
}
