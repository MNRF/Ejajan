package com.mnrf.ejajan.view.main.parent.ui.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.ItemSliderBinding

class ImageSliderAdapter(private val imageList: List<ImageSliderData>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    class ImageSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_slider) // Ensure this ID exists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageData = imageList[position]
        holder.imageView.setImageResource(imageData.image)

        holder.imageView.adjustViewBounds = true
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        // Sesuaikan tinggi ImageView berdasarkan rasio aspek gambar
        holder.imageView.post {
            val aspectRatio = holder.imageView.drawable.intrinsicWidth.toFloat() /
                    holder.imageView.drawable.intrinsicHeight
            val width = holder.itemView.width
            val height = (width / aspectRatio).toInt()
            val layoutParams = holder.imageView.layoutParams
            layoutParams.height = height
            holder.imageView.layoutParams = layoutParams
        }
    }


    override fun getItemCount(): Int = imageList.size
}

