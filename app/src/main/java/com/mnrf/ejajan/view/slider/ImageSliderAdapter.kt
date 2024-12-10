package com.mnrf.ejajan.view.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mnrf.ejajan.R

class ImageSliderAdapter(private val imageList: List<ImageSliderData>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    class ImageSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_slider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageData = imageList[position]
        holder.imageView.setImageResource(imageData.image)

        // Ensure the image scales within bounds.
        holder.imageView.adjustViewBounds = true
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        holder.imageView.post {
            val drawable = holder.imageView.drawable
            if (drawable != null) {
                val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight
                val width = holder.itemView.width
                val height = (width / aspectRatio).toInt()

                val layoutParams = holder.imageView.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = height
                holder.imageView.layoutParams = layoutParams
            }
        }
    }

    override fun getItemCount(): Int = imageList.size
}
