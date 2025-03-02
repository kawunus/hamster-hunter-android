package ru.practicum.android.diploma.filter.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemCountryRegionListBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class RegionsAdapter : RecyclerView.Adapter<RegionsAdapter.RegionsHolder>() {
    private var regionsList = ArrayList<Region>()
    var onItemClick: ((Region) -> Unit)? = null

    inner class RegionsHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemCountryRegionListBinding.bind(item)

        fun bind(region: Region) = with(binding) {
            countryRegionName.text = region.name
            root.setOnClickListener {
                onItemClick?.invoke(region)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country_region_list, parent, false)
        return RegionsHolder(view)
    }

    override fun onBindViewHolder(holder: RegionsHolder, position: Int) {
        holder.bind(regionsList[position])
    }

    override fun getItemCount() = regionsList.size

    fun setData(newList: List<Region>) {
        regionsList.clear()
        regionsList.addAll(newList)
        notifyDataSetChanged()
    }
}
