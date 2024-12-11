package com.mnrf.ejajan.view.main.merchant.ui.home

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.data.model.MerchantOrderModel
import com.mnrf.ejajan.databinding.ItemMerchantOrderListBinding
import com.mnrf.ejajan.view.main.merchant.ui.activeorder.OrderConfirmationActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory
import java.sql.Date
import java.util.Locale

class MerchantHomeAdapter(private val viewModelStoreOwner: ViewModelStoreOwner) :
    ListAdapter<MerchantOrderModel, MerchantHomeAdapter.OrderViewHolder>(DIFF_CALLBACK) {

    private val merchantHomeViewModel: MerchantHomeViewModel by lazy {
        ViewModelProvider(viewModelStoreOwner)[MerchantHomeViewModel::class.java]
    }

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
                val menuPrice = "Rp${order.menuPrice.toLong() * order.menuPrice.toLong()}"
                placeholderPrice.text = menuPrice
                tvOrderStatus.text = order.orderStatus

                btnTerimaPesanan.setOnClickListener {
                    merchantHomeViewModel.acceptOrder(order.id)
                }
                btnTolakPesanan.setOnClickListener {
                    merchantHomeViewModel.declineOrder(order.id)
                }
                btnKonfirmasipengambilan.setOnClickListener { view ->
                    val intent = Intent(view.context, OrderConfirmationActivity::class.java)
                    intent.putExtra(OrderConfirmationActivity.SELECTED, order)
                    this.btnKonfirmasipengambilan.context.startActivity(intent)
                }

                if (order.orderStatus == "Accepted") {
                    btnTerimaPesanan.visibility = View.GONE
                    btnTolakPesanan.visibility = View.GONE
                    btnKonfirmasipengambilan.visibility = View.VISIBLE
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

    override fun submitList(list: List<MerchantOrderModel>?) {
        val filteredList = list?.filter { it.orderStatus == "Pending" || it.orderStatus == "Accepted" }
        super.submitList(filteredList)
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