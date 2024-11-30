package com.mnrf.ejajan.view.main.parent.ui.student.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.databinding.ItemParentAlergiBinding

class AllergyAdapter(private val items: MutableList<AllergyModel>) :
    RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder>() {

    class AllergyViewHolder(val binding: ItemParentAlergiBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
        val binding = ItemParentAlergiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllergyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvAllergyName.text = item.name
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<AllergyModel>) {
        items.clear()
        items.addAll(newItems)
        notifyItemRangeChanged(0, items.size)
    }
}
