package com.mnrf.ejajan.view.main.parent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.databinding.ItemParentHistoryBinding

class HistoryAdapter : ListAdapter<MerchantOrderModel, HistoryAdapter.MenuViewHolder>(DIFF_CALLBACK) {

    inner class MenuViewHolder(private val binding: ItemParentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: MerchantOrderModel) {
            binding.apply {
                Glide.with(binding.tvPlaceholderImage.context)
                    .load(cart.menuImage)
                    .into(binding.tvPlaceholderImage)
                tvPlaceholderName.text = cart.menuName
                val cartPrice = "Rp.${cart.menuPrice}"
                val quantity = cart.menuQty
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MerchantOrderModel>() {
            override fun areItemsTheSame(oldItem: MerchantOrderModel, newItem: MerchantOrderModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MerchantOrderModel, newItem: MerchantOrderModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}