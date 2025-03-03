package ru.practicum.android.diploma.filter.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Industry

class IndustryViewHolder(
    private val binding: ItemIndustryBinding,
    private val onItemClicked: (Industry) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industry, isSelected: Boolean, onItemSelected: (Industry) -> Unit) {
        binding.industryName.text = industry.name
        binding.radioButton.isChecked = isSelected

        val clickListener = {
            if (!isSelected) {
                onItemSelected(industry)
            }
        }

        binding.radioButton.setOnClickListener { clickListener() }
        binding.root.setOnClickListener { clickListener() }
    }
}
