package com.mnrf.ejajan.view.main.merchant.ui.menu

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.databinding.ItemMerchantMenuBinding
import com.mnrf.ejajan.view.main.merchant.ui.menu.detail.DetailMenuActivity

class MenuListAdapter : ListAdapter<MenuModel, MenuListAdapter.MenuViewHolder>(DIFF_CALLBACK) {

    inner class MenuViewHolder(private val binding: ItemMerchantMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuModel) {
            binding.apply {
                Glide.with(binding.placeholderImage.context)
                    .load(menu.imageurl)
                    .into(binding.placeholderImage)
                placeholderName.text = menu.name
                val menuPrice = "Rp.${menu.price}"
                val prepTime = "${menu.preparationtime} minute"
                placeholderPrice.text = menuPrice
                placeholderTime.text = prepTime
                cvItemMerchantMenu.setOnClickListener{ view ->
                    val intent = Intent(view.context, DetailMenuActivity::class.java)
                    intent.putExtra(DetailMenuActivity.MENU_ID, menu.id)
                    intent.putExtra(DetailMenuActivity.MENU_NAME, menu.name)
                    intent.putExtra(DetailMenuActivity.MENU_DESCRIPTION, menu.description)
                    intent.putExtra(DetailMenuActivity.MENU_INGREDIENTS, menu.ingredients)
                    intent.putExtra(DetailMenuActivity.MENU_PREPARATIONTIME, menu.preparationtime)
                    intent.putExtra(DetailMenuActivity.MENU_PRICE, menu.price)
                    intent.putExtra(DetailMenuActivity.MENU_IMAGE, menu.imageurl)
                    this.cvItemMerchantMenu.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMerchantMenuBinding.inflate(
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