package com.mnrf.ejajan.view.main.parent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.databinding.ItemParentHistoryBinding

class HistoryAdapter : ListAdapter<CartModel, HistoryAdapter.MenuViewHolder>(DIFF_CALLBACK) {

    inner class MenuViewHolder(private val binding: ItemParentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: CartModel) {
            binding.apply {
                Glide.with(binding.placeholderImage.context)
                    .load(cart.imageurl)
                    .into(binding.placeholderImage)
                tvPlaceholderName.text = cart.name
                val cartPrice = "Rp.${cart.price}"
                val quantity = cart.quantity
                tvPlaceholderPrice.text = cartPrice
                tvPlaceholderQuantity.text = quantity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemParentHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartModel>() {
            override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}