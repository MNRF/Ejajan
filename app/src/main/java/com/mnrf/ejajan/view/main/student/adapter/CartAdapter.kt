package com.mnrf.ejajan.view.main.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.CartModel
import com.mnrf.ejajan.databinding.ItemCartBinding

class CartAdapter(private val cartItems: List<CartModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartModel) {
            with(binding) {
                Glide.with(binding.placeholderImage.context)
                    .load(item.imageurl)
                    .into(binding.placeholderImage)
                tvItemName.text = item.name
                tvItemPrice.text = root.context.getString(R.string.price_format, item.price)
                tvItemQuantity.text = root.context.getString(R.string.quantity_format, item.quantity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
