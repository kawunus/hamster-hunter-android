package ru.practicum.android.diploma.filter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.model.Country

class CountryAdapter(
    private val onItemClicked: (Country) -> Unit
) : RecyclerView.Adapter<CountryViewHolder>() {

    private val items = mutableListOf<Country>()

    fun setData(newItems: List<Country>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_country_region_list, parent, false)
        return CountryViewHolder(itemView, onItemClicked)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
