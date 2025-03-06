package ru.practicum.android.diploma.vacancy.presentation.ui.fragment

import android.text.Spanned
import androidx.core.text.HtmlCompat
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
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyDetailsState
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(
    inflate = FragmentVacancyBinding::inflate
) {
    override val viewModel: VacancyViewModel by viewModel {
        val args by navArgs<VacancyFragmentArgs>()
        val vacancyId by lazy { args.vacancyId }
        parametersOf(vacancyId)
    }

    override fun initViews() {
        bindButtons()
    }

    override fun subscribe() {
        viewModel.observeVacancyDetailsState().observe(viewLifecycleOwner) { state ->
            renderVacancyState(state)
        }
        viewModel.observeIsLikedLiveData().observe(viewLifecycleOwner) { state ->
            renderLikeStatus(state)
        }
    }

    private fun bindButtons() = with(binding) {
        buttonBack.setOnClickListener { findNavController().navigateUp() }
        buttonShare.setOnClickListener { viewModel.shareVacancyUrl() }
        buttonLike.setOnClickListener { viewModel.favoritesHandler() }
    }

    private fun renderVacancyInfo(vacancyDetails: VacancyDetails) = with(binding) {
        name.text = vacancyDetails.name
        salary.text =
            formatSalary(vacancyDetails.salaryFrom, vacancyDetails.salaryTo, vacancyDetails.currency, requireContext())
        employerName.text = vacancyDetails.employer
        showAddress(vacancyDetails.area, vacancyDetails.city, vacancyDetails.street, vacancyDetails.building)
        experience.text = vacancyDetails.experience
        showEmploymentFormAndWorkFormat(vacancyDetails.employment, vacancyDetails.workFormat)
        jobDescription.text = createDescription(vacancyDetails.description)
        // загружаю ключевые скиллы
        showKeySkills(vacancyDetails.keySkills)
        // загружаю иконку
        Glide.with(requireContext())
            .load(vacancyDetails.icon)
            .placeholder(R.drawable.placeholder_32px)
            .fitCenter()
            .into(employerImg)
    }

    private fun showOrHideError(isErrorVisible: Boolean) = with(binding) {
        errorBox.isVisible = isErrorVisible
        jobInfo.isVisible = !isErrorVisible
    }

    private fun showErrorNoInternet() = with(binding) {
        showOrHideError(true)
        progressBar.hide()
        errorImg.setImageResource(R.drawable.placeholder_network_error)
        errorText.text = getString(R.string.error_no_internet)
        buttonShare.hide()
        buttonLike.hide()
    }

    private fun showErrorServer() = with(binding) {
        showOrHideError(true)
        progressBar.hide()
        errorImg.setImageResource(R.drawable.placeholder_server_vacancy_error)
        errorText.text = getString(R.string.error_server)
        buttonShare.hide()
        buttonLike.hide()
    }

    private fun showErrorNotFound() = with(binding) {
        showOrHideError(true)
        progressBar.hide()
        errorImg.setImageResource(R.drawable.placeholder_job_deleted_error)
        errorText.text = getString(R.string.error_job_not_found_or_deleted)
    }

    private fun showLoading() = with(binding) {
        showOrHideError(false)
        jobInfo.hide()
        progressBar.show()
    }

    private fun showVacancyDetails(vacancyDetailsState: VacancyDetailsState.VacancyInfo) = with(binding) {
        showOrHideError(false)
        progressBar.hide()
        renderVacancyInfo(vacancyDetailsState.details)

    }

    private fun showAddress(area: String, city: String, street: String, building: String) = with(binding) {
        var newAddress = area
        if (city.isEmpty()) { // нету точного адреса
        } else {
            newAddress = city
        }
        if (street.isNotEmpty()) {
            newAddress += Constants.PUNCTUATION + street
        }
        if (building.isNotEmpty() && street.isNotEmpty()) { // дом нужен только если известна улица
            newAddress += Constants.PUNCTUATION + building
        }
        employerLocation.text = newAddress
    }

    private fun showKeySkills(currentKeySkills: List<String>) = with(binding) {
        var keySkillsText = Constants.EMPTY_STRING
        for (i in currentKeySkills) {
            keySkillsText += getString(R.string.key_skill_separator, i)
        }
        keySkills.text = keySkillsText
        keySkillsTitle.isVisible = currentKeySkills.isNotEmpty()
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

    private fun renderLikeStatus(isLiked: Boolean?) = with(binding) {
        if (isLiked != null) {
            val iconRes = if (isLiked) {
                R.drawable.ic_favorites_on
            } else {
                R.drawable.ic_favorites_off
            }
            buttonLike.setImageResource(iconRes)
        }
    }

    private fun renderVacancyState(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()

            is VacancyDetailsState.NotFoundError -> showErrorNotFound()

            is VacancyDetailsState.ServerError -> showErrorServer()

            is VacancyDetailsState.VacancyInfo -> showVacancyDetails(state)

            is VacancyDetailsState.NoInternet -> showErrorNoInternet()
        }
    }

    private fun createDescription(htmlString: String?): Spanned {
        if (htmlString.isNullOrEmpty()) {
            binding.jobDescription.hide()
        }

        val formattedHtml = htmlString
            ?.replace(Regex(getString(R.string.description_regex_li)), getString(R.string.description_li_replacement))
            ?: Constants.EMPTY_STRING

        return HtmlCompat.fromHtml(formattedHtml, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM)
    }
}
