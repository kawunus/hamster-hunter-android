package ru.practicum.android.diploma.filter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Industry

class IndustryAdapter(private val onItemClick: ((industry: Industry) -> Unit)) :
    RecyclerView.Adapter<IndustryViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Industry>() {

        override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(industriesList: List<Industry>) {
        asyncListDiffer.submitList(industriesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding, onItemClick)

    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

}
