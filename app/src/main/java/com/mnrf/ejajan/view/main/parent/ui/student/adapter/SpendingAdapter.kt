package com.mnrf.ejajan.view.main.parent.ui.student.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.SpendingModel
import com.mnrf.ejajan.databinding.ItemParentSpendingBinding

class SpendingAdapter(private val items: MutableList<SpendingModel>) :
    RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder>() {

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
        val item = items[position]
        val combinedText = "${item.amount} ${item.period}"
        holder.binding.tvSpending.text = combinedText
        holder.binding.tvSpending.setTextColor(Color.WHITE)
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<SpendingModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}