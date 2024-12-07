package com.mnrf.ejajan.view.main.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.OrderSummaryModel
import com.mnrf.ejajan.databinding.ItemOrderSummaryBinding

class OrderSummaryAdapter : RecyclerView.Adapter<OrderSummaryAdapter.NotesViewHolder>() {

    private var notesList = listOf<OrderSummaryModel>()

    fun submitList(list: List<OrderSummaryModel>) {
        notesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemOrderSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notesList.getOrNull(position) ?: return
        holder.bind(note)
    }

    override fun getItemCount(): Int = notesList.size

    inner class NotesViewHolder(private val binding: ItemOrderSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: OrderSummaryModel) {
            binding.tvNotes.text = note.name
            binding.tvPickupTime.text = note.preparationTime
        }
    }
}
