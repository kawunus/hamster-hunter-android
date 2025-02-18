package ru.practicum.android.diploma.search.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.root.ui.dpToPx
import ru.practicum.android.diploma.search.presentation.model.Vacancy
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.formatSalary

class VacancyViewHolder(
    private val binding: VacancyItemBinding,
    private val onVacancyClick: (Vacancy) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) {
        binding.vacancyNameAndLocation.text = "${vacancy.name}, ${vacancy.area}"
        binding.companyName.text = vacancy.company

        binding.salary.text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.currency, itemView.context)

        Glide.with(itemView)
            .load(vacancy.icon)
            .placeholder(R.drawable.placeholder_32px)
            .fitCenter()
            .transform(RoundedCorners(itemView.context.dpToPx(Constants.CORNER_RADIUS))).into(binding.companyIcon)

        binding.root.setOnClickListener { onVacancyClick(vacancy) }
    }
}
