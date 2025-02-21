package ru.practicum.android.diploma.vacancy.presentation.ui.fragment

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.formatSalary
import ru.practicum.android.diploma.vacancy.presentation.viewmodel.VacancyViewModel

class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(
    inflate = FragmentVacancyBinding::inflate
) {
    override val viewModel: VacancyViewModel by inject()

    override fun initViews() {
        bindButtons()
        val args by navArgs<VacancyFragmentArgs>()
        val vacancyId by lazy { args.vacancyId }
        testValues(vacancyId.toString())
    }

    override fun subscribe() = with(binding) {
        viewModel.observeIsFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                buttonLike.setImageResource(R.drawable.ic_favorites_on)
            } else {
                buttonLike.setImageResource(R.drawable.ic_favorites_off)
            }
        }
    }

    private fun bindButtons() = with(binding) {
        buttonBack.setOnClickListener { findNavController().navigateUp() }

        // Потом необходимо убрать navArgs отсюда
        val args by navArgs<VacancyFragmentArgs>()
        buttonLike.setOnClickListener {
            viewModel.likeButtonControl(
                vacancy = Vacancy(
                    id = args.vacancyId!!,
                    name = getString(
                        R.string.vacancy_name_and_location, NAMETEST, args.vacancyId
                    ),
                    salaryTo = null,
                    salaryFrom = 0,
                    company = "Yandex",
                    area = "Москва",
                    currency = "BYN",
                    icon = ""
                )
            )
        }
    }

    private fun testValues(idString: String) {
        when (TESTTYPE) {
            0 -> {
                renderError(true)
                binding.progressBar.isVisible = false
                changeErrorMessage(true)
            }

            1 -> {
                renderError(true)
                binding.progressBar.isVisible = false
                changeErrorMessage(false)
            }

            2 -> {
                renderError(false)
                binding.jobInfo.isVisible = false
                binding.progressBar.isVisible = true
            }

            else -> {
                renderError(false)
                binding.progressBar.isVisible = false
                renderTestInfo(idString)
            }
        }

    }

    private fun renderTestInfo(idString: String) {
        binding.name.text = getString(
            R.string.vacancy_name_and_location, NAMETEST, idString
        )
        binding.salary.text = formatSalary(0, null, "USD", requireContext())
        binding.employerName.text = EMPLNAMETEST
        binding.employerLocation.text = EMPLAREATEST
        binding.experience.text = EXPTEST
        binding.employmentFormAndWorkFormat.text = getString(
            R.string.vacancy_name_and_location, EMPLJOBFORMAT1TEST,
            EMPLJOBFORMAT2TEST
        )
        binding.jobDescription.text = DESCRIPTIONTEST
        // загружаю ключевые скиллы
        var keySkills = ""
        for (i in testArray) {
            keySkills += getString(R.string.key_skill_separator, i)
        }
        binding.keySkills.text = keySkills
        // загружаю иконку
        Glide.with(this.requireContext())
            .load(IMGTEST)
            .placeholder(R.drawable.placeholder_32px)
            .fitCenter()
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

    companion object {
        const val TESTTYPE =
            3 // 3 и более -без ошибок, 2 - загрузка 1-ошибка вакансия удалена или нет в базе, 0 -ошибка сервера
        const val NAMETEST = "Хомяк ID"
        const val IMGTEST = "https://hh.ru/employer-logo/289027.png"
        const val EMPLNAMETEST = "ХомякПромПрог"
        const val EMPLAREATEST = "Рай Программистов"
        const val EXPTEST = "От 1 мес опыта"
        const val EMPLJOBFORMAT1TEST = "Полная занятость"
        const val EMPLJOBFORMAT2TEST = "Без графика"
        const val DESCRIPTIONTEST = "стать программистом"
        val testArray = arrayOf(
            "крутить педали",
            "быть хомяком",
            "внизу проверка прокрутки\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n",
            "тест прокрутки"
        )
    }
}
