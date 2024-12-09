package com.mnrf.ejajan.view.main.parent.ui.student.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.databinding.ItemParentNutritionBinding

class NutritionAdapter(
    private val items: MutableList<Pair<NutritionModel, String>> // Pair of NutritionModel and Child Name
) : RecyclerView.Adapter<NutritionAdapter.NutritionViewHolder>() {

    class NutritionViewHolder(val binding: ItemParentNutritionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        val binding = ItemParentNutritionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val (nutrition, childName) = items[position]
        holder.binding.tvNutrition.text = "$childName: ${nutrition.name}"
        holder.binding.tvNutrition.setTextColor(Color.DKGRAY)
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Pair<NutritionModel, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // Refresh the entire list
    }

}
