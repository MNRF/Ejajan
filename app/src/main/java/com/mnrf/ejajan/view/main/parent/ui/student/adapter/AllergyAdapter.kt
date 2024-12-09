package com.mnrf.ejajan.view.main.parent.ui.student.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.data.model.AllergyModel
import com.mnrf.ejajan.databinding.ItemParentAlergiBinding

class AllergyAdapter(
    private val items: MutableList<Pair<AllergyModel, String>> // Pair of AllergyModel and Child Name
) : RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder>() {

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
        val (allergy, childName) = items[position]
        holder.binding.tvAllergyName.text = "$childName: ${allergy.name}"
        holder.binding.tvAllergyName.setTextColor(Color.DKGRAY)
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Pair<AllergyModel, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // Refresh the entire list
    }
}
