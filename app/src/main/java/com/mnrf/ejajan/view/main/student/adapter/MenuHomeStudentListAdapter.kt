package com.mnrf.ejajan.view.main.student.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.MenuModel
import com.mnrf.ejajan.databinding.ItemHomeMenuBinding
import com.mnrf.ejajan.view.main.student.detail.DetailMenuStudentActivity

class MenuHomeStudentListAdapter : ListAdapter<MenuModel, MenuHomeStudentListAdapter.MenuViewHolder>(DIFF_CALLBACK) {

    inner class MenuViewHolder(private val binding: ItemHomeMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuModel) {
            binding.apply {
                Glide.with(binding.placeholderImage.context)
                    .load(menu.imageurl)
                    .into(binding.placeholderImage)
                tvPlaceholderName.text = menu.name
                val menuPrice = "Rp.${menu.price}"
                val prepTime = "${menu.preparationtime} minute"
                tvPlaceholderPrice.text = menuPrice
                tvPlaceholderTime.text = prepTime
                cvHomeMenu.setOnClickListener{ view ->
                    val intent = Intent(view.context, DetailMenuStudentActivity::class.java)
                    intent.putExtra(DetailMenuStudentActivity.MENU_ID, menu.id)
                    intent.putExtra(DetailMenuStudentActivity.MENU_NAME, menu.name)
                    intent.putExtra(DetailMenuStudentActivity.MENU_DESCRIPTION, menu.description)
                    intent.putExtra(DetailMenuStudentActivity.MENU_PREPARATIONTIME, menu.preparationtime)
                    intent.putExtra(DetailMenuStudentActivity.MENU_PRICE, menu.price)
                    intent.putExtra(DetailMenuStudentActivity.MENU_IMAGE, menu.imageurl)
                    this.cvHomeMenu.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemHomeMenuBinding.inflate(
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