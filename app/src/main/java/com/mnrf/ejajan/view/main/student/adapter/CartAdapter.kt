package com.mnrf.ejajan.view.main.student.adapter

import android.view.LayoutInflater
import android.view.View
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
                // Load image
                Glide.with(binding.placeholderImage.context)
                    .load(item.imageurl)
                    .into(binding.placeholderImage)

                // Set item name
                tvItemName.text = item.name

                // Check if discount is available
                val discountedPrice = item.discountedPrice?.toDoubleOrNull()
                if (discountedPrice != null && discountedPrice > 0) {
                    // Show original price with strike-through
                    tvItemOriginalPrice.apply {
                        text = root.context.getString(R.string.price_format_special, item.price.toInt())
                        paintFlags = paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                        setTextColor(root.context.getColor(R.color.gray))
                        visibility = View.VISIBLE
                    }

                    // Show discounted price
                    tvItemPrice.apply {
                        text = root.context.getString(R.string.price_format_special, discountedPrice.toInt())
                        setTextColor(root.context.getColor(R.color.red))
                        visibility = View.VISIBLE
                    }
                } else {
                    // Show only the original price
                    tvItemOriginalPrice.visibility = View.GONE
                    tvItemPrice.apply {
                        text = root.context.getString(R.string.price_format_special, item.price.toInt())
                        setTextColor(root.context.getColor(R.color.black))
                        visibility = View.VISIBLE
                    }
                }

                // Set item quantity
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
