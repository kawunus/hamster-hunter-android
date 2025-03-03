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

    private var selectedIndustryId: String? = null

    fun saveData(industriesList: List<Industry>) {
        asyncListDiffer.submitList(industriesList)
    }

    fun setSelectedIndustry(id: String?) {
        val previousSelectedId = selectedIndustryId
        selectedIndustryId = id

        asyncListDiffer.currentList.indexOfFirst { it.id == previousSelectedId }
            .takeIf { it != -1 }
            ?.let { notifyItemChanged(it) }

        asyncListDiffer.currentList.indexOfFirst { it.id == selectedIndustryId }
            .takeIf { it != -1 }
            ?.let { notifyItemChanged(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = asyncListDiffer.currentList[position]
        holder.bind(industry, industry.id == selectedIndustryId) {
            val previousSelectedId = selectedIndustryId
            selectedIndustryId = industry.id

            asyncListDiffer.currentList.indexOfFirst { it.id == previousSelectedId }
                .takeIf { it != -1 }
                ?.let { notifyItemChanged(it) }

            notifyItemChanged(holder.bindingAdapterPosition)

            onItemClick(industry)
        }
    }
}

