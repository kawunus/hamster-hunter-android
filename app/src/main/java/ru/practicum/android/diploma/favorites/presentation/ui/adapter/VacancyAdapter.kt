package ru.practicum.android.diploma.favorites.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.presentation.ui.adapter.VacancyViewHolder

class VacancyAdapter(private val onItemClick: ((vacancy: Vacancy) -> Unit)) :
    RecyclerView.Adapter<VacancyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Vacancy>() {

        override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(vacanciesList: List<Vacancy>) {
        asyncListDiffer.submitList(vacanciesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding, onItemClick)

    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

}
