package ru.practicum.android.diploma.vacancy.presentation.ui.fragment

import android.text.Html
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.formatSalary
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetailsLikeState
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
        viewModel.observeVacancyDetailsLikeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyDetailsLikeState.Liked -> changeLikeStatus(true)
                is VacancyDetailsLikeState.NotLiked -> changeLikeStatus(false)
                else -> {}
            }
        }
    }

    private fun bindButtons() = with(binding) {
        buttonBack.setOnClickListener { findNavController().navigateUp() }
        buttonShare.setOnClickListener { viewModel.shareControll() }
        buttonLike.setOnClickListener { viewModel.likeControl() }
    }

    private fun renderVacancyInfo(vacancyDetails: VacancyDetails) = with(binding) {
        name.text = vacancyDetails.name
        salary.text =
            formatSalary(vacancyDetails.salaryFrom, vacancyDetails.salaryTo, vacancyDetails.currency, requireContext())
        employerName.text = vacancyDetails.employer
        employerLocation.text =
            findAddress(vacancyDetails.area, vacancyDetails.city, vacancyDetails.street, vacancyDetails.building)
        experience.text = vacancyDetails.experience
        showEmploymentFormAndWorkFormat(vacancyDetails.employment, vacancyDetails.workFormat)
        // !!!!!!!!!!!!!!!----не забыть дополнить по выполнению коллегами таска 47------!!!!!!!!!!!!
        // ЭТО ВРЕМЕННОЕ РЕШЕНИЕ! КАК ИСПРАЯТ УДАЛИТЬ ИМПОРТ Html !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        jobDescription.text = Html.fromHtml(vacancyDetails.description, Html.FROM_HTML_MODE_LEGACY).toString()
        // загружаю ключевые скиллы
        showKeySkills(vacancyDetails.keySkills)
        // загружаю иконку
        Glide.with(requireContext())
            .load(vacancyDetails.icon)
            .placeholder(R.drawable.placeholder_32px)
            .fitCenter()
            .into(employerImg)
    }

    private fun renderError(showErrorOrNot: Boolean) = with(binding) {
        errorBox.isVisible = showErrorOrNot
        jobInfo.isVisible = !showErrorOrNot
    }

    private fun changeErrorMessage(isServerError: Boolean) = with(binding) {
        if (isServerError) {
            errorImg.setImageResource(R.drawable.placeholder_server_vacancy_error)
            errorText.text = getString(R.string.error_server)
        } else {
            errorImg.setImageResource(R.drawable.placeholder_job_deleted_error)
            errorText.text = getString(R.string.error_job_not_found_or_deleted)
        }
    }

    private fun showErrorServer() = with(binding) {
        renderError(true)
        progressBar.isVisible = false
        changeErrorMessage(true)
    }

    private fun showErrorNotFound() = with(binding) {
        renderError(true)
        progressBar.isVisible = false
        changeErrorMessage(false)
    }

    private fun showLoading() = with(binding) {
        renderError(false)
        jobInfo.isVisible = false
        progressBar.isVisible = true
    }

    private fun showVacancyDetails(vacancyDetailsState: VacancyDetailsState.VacancyLiked) = with(binding) {
        renderError(false)
        progressBar.isVisible = false
        renderVacancyInfo(vacancyDetailsState.details)

    }

    private fun findAddress(area: String, city: String, street: String, building: String): String {
        var newAddress = area
        if (city.isEmpty()) {
            return newAddress // нету точного адреса
        } else {
            newAddress = city
        }
        if (street.isEmpty()) {
            return city
        } else {
            newAddress += Constants.PUNCTUATION + street
        }
        if (building.isNotEmpty()) {
            newAddress += Constants.PUNCTUATION + building
        }
        return newAddress
    }

    private fun showKeySkills(currentKeySkills: List<String>) = with(binding) {
        var keySkillsText = Constants.EMPTY_STRING
        for (i in currentKeySkills) {
            keySkillsText += getString(R.string.key_skill_separator, i)
        }
        keySkills.text = keySkillsText
        keySkillsTitle.isVisible = if (currentKeySkills.size == 0) false else true
    }

    private fun showEmploymentFormAndWorkFormat(employmentForm: String, currentWorkFormat: List<String>) =
        with(binding) {
            var employmentOptions = Constants.EMPTY_STRING
            employmentOptions = if (employmentForm.isNotEmpty()) {
                employmentForm +
                    if (currentWorkFormat.isNotEmpty()) Constants.PUNCTUATION else Constants.EMPTY_STRING
            } else {
                employmentOptions
            }
            currentWorkFormat.forEachIndexed { index, s ->
                employmentOptions += s
                if (index < currentWorkFormat.size - 1) employmentOptions += Constants.PUNCTUATION
            }
            employmentFormAndWorkFormat.text = employmentOptions
        }

    private fun changeLikeStatus(isLiked: Boolean) = with(binding) {
        val iconRes = if (isLiked) {
            R.drawable.ic_favorites_on
        } else {
            R.drawable.ic_favorites_off
        }
        buttonLike.setImageResource(iconRes)
    }
}
