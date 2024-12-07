package com.mnrf.ejajan.view.main.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.NotesModel
import com.mnrf.ejajan.databinding.ItemNotesBinding

class NotesAdapter : ListAdapter<NotesModel, NotesAdapter.MenuViewHolder>(DIFF_CALLBACK) {
    private val selectedItems = mutableSetOf<Int>()

    inner class MenuViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notes: NotesModel, isSelected: Boolean) {
            binding.apply {
                tvNote.text = notes.name
                root.isSelected = isSelected

                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (selectedItems.contains(position)) {
                        selectedItems.remove(position)
                    } else {
                        selectedItems.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position), selectedItems.contains(position))
    }

    fun getSelectedItems(): List<NotesModel> {
        return selectedItems.map { getItem(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotesModel>() {
            override fun areItemsTheSame(oldItem: NotesModel, newItem: NotesModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NotesModel, newItem: NotesModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
