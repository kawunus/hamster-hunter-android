package ru.practicum.android.diploma.favorites.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.dpToPx
import ru.practicum.android.diploma.util.formatSalary

class FavoriteVacancyViewHolder(
    private val binding: VacancyItemBinding,
    private val onVacancyClick: (FavoriteVacancy) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: FavoriteVacancy) {
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
                .transform(RoundedCorners(itemView.context.dpToPx(Constants.CORNER_RADIUS))).into(companyIcon)

            root.setOnClickListener { onVacancyClick(vacancy) }
        }
    }
}
