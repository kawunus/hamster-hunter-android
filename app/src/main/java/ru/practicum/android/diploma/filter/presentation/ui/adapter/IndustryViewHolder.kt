package ru.practicum.android.diploma.filter.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Industry

class IndustryViewHolder(
    private val binding: ItemIndustryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industry, isSelected: Boolean, onItemSelected: () -> Unit) {
        binding.industryName.text = industry.name
        binding.radioButton.isChecked = isSelected

        val clickListener = {
            if (!isSelected) {
                onItemSelected()
            }
        }

        binding.radioButton.setOnClickListener { clickListener() }
        binding.root.setOnClickListener { clickListener() }
    }
}

