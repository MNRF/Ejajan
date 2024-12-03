package com.mnrf.ejajan.view.main.parent.ui.student.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.NutritionModel
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.databinding.ItemParentNutritionBinding
import com.mnrf.ejajan.databinding.ItemParentSpendingBinding
import com.mnrf.ejajan.view.main.parent.ui.student.adapter.SpendingAdapter.SpendingViewHolder

class NutritionAdapter (private val items: MutableList<NutritionModel>) :
    RecyclerView.Adapter<NutritionAdapter.NutritionViewHolder>(){

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
        val item = items[position]
        val combinedText = "${item.name} & ${item.mineral}"
        holder.binding.tvNutrition.text = combinedText
        holder.binding.tvNutrition.setTextColor(Color.WHITE)
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<NutritionModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}