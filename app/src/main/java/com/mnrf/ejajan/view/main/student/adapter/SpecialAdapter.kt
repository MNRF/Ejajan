package com.mnrf.ejajan.view.main.student.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.R
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.databinding.ItemHomeMenuBinding
import com.mnrf.ejajan.databinding.ItemSpecialBinding
import com.mnrf.ejajan.view.main.student.detail.DetailMenuSpecialActivity
import com.mnrf.ejajan.view.main.student.detail.DetailMenuStudentActivity

class SpecialAdapter : ListAdapter<MenuModel, SpecialAdapter.MenuViewHolder>(DIFF_CALLBACK) {

    inner class MenuViewHolder(private val binding: ItemSpecialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuModel) {
            binding.apply {
                Glide.with(binding.placeholderImage.context)
                    .load(menu.imageurl)
                    .into(binding.placeholderImage)
                tvPlaceholderName.text = menu.name

                val originalPrice = menu.price.toDoubleOrNull() ?: 0.0
                val discount = 10
                val discountedPrice = originalPrice - (originalPrice * discount / 100)

                // Set harga asli dengan coretan
                tvPlaceholderOriginalPrice.apply {
                    text = context.getString(R.string.original_price_format, originalPrice.toInt())
                    setTextColor(0xFF999999.toInt())
                    paintFlags = paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                }

                // Set harga setelah diskon
                tvPlaceholderDiscountedPrice.apply {
                    text = context.getString(R.string.discounted_price_format, discountedPrice.toInt())
                    setTextColor(0xFFFF0000.toInt())
                }

                val prepTime = "${menu.preparationtime} minute"
                tvPlaceholderTime.text = prepTime

                cvHomeMenu.setOnClickListener{ view ->
                    val intent = Intent(view.context, DetailMenuSpecialActivity::class.java)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_ID, menu.id)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_NAME, menu.name)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_DESCRIPTION, menu.description)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_PREPARATIONTIME, menu.preparationtime)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_PRICE, menu.price)
                    intent.putExtra(DetailMenuSpecialActivity.MENU_IMAGE, menu.imageurl)
                    this.cvHomeMenu.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemSpecialBinding.inflate(
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuModel>() {
            override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}