package ru.practicum.android.diploma.favorites.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.favorites.domain.model.FavoriteVacancy

class VacancyAdapter(private val onItemClick: ((vacancy: FavoriteVacancy) -> Unit)) :
    RecyclerView.Adapter<FavoriteVacancyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<FavoriteVacancy>() {

        override fun areItemsTheSame(oldItem: FavoriteVacancy, newItem: FavoriteVacancy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteVacancy, newItem: FavoriteVacancy): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(vacanciesList: List<FavoriteVacancy>) {
        asyncListDiffer.submitList(vacanciesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVacancyViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteVacancyViewHolder(binding, onItemClick)

    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: FavoriteVacancyViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

}
