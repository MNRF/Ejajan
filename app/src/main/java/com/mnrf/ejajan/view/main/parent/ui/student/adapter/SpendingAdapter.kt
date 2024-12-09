package com.mnrf.ejajan.view.main.parent.ui.student.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.databinding.ItemParentSpendingBinding

class SpendingAdapter(
    private val items: MutableList<Pair<SpendingModel, String>> // Pair of SpendingModel and Child Name
) : RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder>() {

    class SpendingViewHolder(val binding: ItemParentSpendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        val binding = ItemParentSpendingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SpendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        val (spending, childName) = items[position]
        val combinedText = "$childName: ${spending.amount} (${spending.period})"
        holder.binding.tvSpending.text = combinedText
        holder.binding.tvSpending.setTextColor(Color.DKGRAY)
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Pair<SpendingModel, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // Refresh the entire list
    }

}
