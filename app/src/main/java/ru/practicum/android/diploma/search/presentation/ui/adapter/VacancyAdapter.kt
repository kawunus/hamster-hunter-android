package ru.practicum.android.diploma.search.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.presentation.model.Vacancy

class VacancyAdapter(
    private val onVacancyClick: (Vacancy) -> Unit
) : PagingDataAdapter<Vacancy, VacancyViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding, onVacancyClick)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class VacancyDiffCallback : DiffUtil.ItemCallback<Vacancy>() {
    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem == newItem
    }
}
