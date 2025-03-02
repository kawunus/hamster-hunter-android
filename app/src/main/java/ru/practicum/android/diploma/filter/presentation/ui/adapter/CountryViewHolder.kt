package ru.practicum.android.diploma.filter.presentation.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.model.Country

class CountryViewHolder(
    itemView: View,
    private val onItemClicked: (Country) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val countryNameTextView: TextView = itemView.findViewById(R.id.country_region_name)

    fun bind(country: Country) {
        // Устанавливаем название страны
        countryNameTextView.text = country.name

        // По клику на весь элемент списка
        itemView.setOnClickListener {
            onItemClicked(country)
        }
    }
}
