package ru.practicum.android.diploma.filter.presentation.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Industry

class IndustryViewHolder(
    itemView: View,
    private val onItemClicked: (Industry) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemIndustryBinding.bind(itemView)

    fun bind(industry: Industry) {
        // Устанавливаем название отрасли
        binding.industryName.text = industry.name

        // По клику на весь элемент списка
        binding.radioButton.setOnClickListener {
            onItemClicked(industry)
        }
    }
}
