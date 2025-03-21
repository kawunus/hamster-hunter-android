package ru.practicum.android.diploma.search.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.formatSalary

class VacancyViewHolder(
    private val binding: ItemVacancyBinding,
    private val onVacancyClick: (Vacancy) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) {
        with(binding) {
            vacancyNameAndLocation.text =
                when (vacancy.area) {
                    Constants.EMPTY_STRING -> vacancy.name
                    else -> itemView.context.getString(
                        R.string.vacancy_name_and_location,
                        vacancy.name,
                        vacancy.area
                    )
                }

            companyName.text = vacancy.company

            salary.text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.currency, itemView.context)

            Glide.with(itemView)
                .load(vacancy.icon)
                .placeholder(R.drawable.placeholder_32px)
                .fitCenter()
                .into(companyIcon)

            root.setOnClickListener { onVacancyClick(vacancy) }
        }
    }
}
