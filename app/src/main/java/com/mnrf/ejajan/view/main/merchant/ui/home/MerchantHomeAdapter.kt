package com.mnrf.ejajan.view.main.merchant.ui.home

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.databinding.ItemMerchantOrderListBinding
import java.sql.Date
import java.util.Locale

class MerchantHomeAdapter : ListAdapter<MerchantOrderModel, MerchantHomeAdapter.OrderViewHolder>(DIFF_CALLBACK) {

    inner class OrderViewHolder(private val binding: ItemMerchantOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: MerchantOrderModel) {
            binding.apply {
                Glide.with(binding.placeholderImage.context)
                    .load(order.menuImage)
                    .into(binding.placeholderImage)
                placeholderName.text = order.menuName
                val pickupTimeInMillis = order.orderPickupTime
                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedTime = timeFormatter.format(Date(pickupTimeInMillis.toLong()))
                val time = "Diambil jam $formattedTime"
                tvOrderPickupTime.text = time
                val menuPrice = "Rp${order.menuPrice}"
                placeholderPrice.text = menuPrice
                tvOrderStatus.text = order.orderStatus
                btnTerimaPesanan.setOnClickListener {
                    //TODO add accept order logic

                    btnTerimaPesanan.visibility = View.GONE
                    btnTolakPesanan.visibility = View.GONE
                    btnKonfirmasipengambilan.visibility = View.VISIBLE
                }
                btnTolakPesanan.setOnClickListener {
                    //TODO add cancel order logic
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemMerchantOrderListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MerchantOrderModel>() {
            override fun areItemsTheSame(oldItem: MerchantOrderModel, newItem: MerchantOrderModel): Boolean {
                return oldItem.orderStatus == newItem.orderStatus
            }

            override fun areContentsTheSame(oldItem: MerchantOrderModel, newItem: MerchantOrderModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}