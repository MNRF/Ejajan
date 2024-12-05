package com.mnrf.ejajan.view.main.merchant.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentMerchantHomeBinding
import com.mnrf.ejajan.view.main.merchant.ui.activeorder.OrderListActivity
import com.mnrf.ejajan.view.slider.ImageSliderAdapter
import com.mnrf.ejajan.view.slider.ImageSliderData

class HomeMerchantFragment : Fragment() {

    private var _binding: FragmentMerchantHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private val listImage = ArrayList<ImageSliderData>()
    private lateinit var circleImage: ArrayList<TextView>

    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable {
        binding.vpMerchantLogo.currentItem =
            (binding.vpMerchantLogo.currentItem + 1) % listImage.size
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupImageSlider()

        binding.tvMerchantWelcome.text = getString(R.string.welcome_merchant)
        binding.tvMerchantDeskripsi.text = getString(R.string.deskripsi_homeMerchant)
        binding.tvSaldo.text = getString(R.string.saldo_rp_10_000)
        binding.tvStatusPesanan.text = getString(R.string.status_pesanan_aktif)

        binding.seeallStatus.setOnClickListener {
            // Tambahkan logika di sini untuk aksi tombol
            val intent = Intent(requireContext(), OrderListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupImageSlider() {
        // Tambahkan gambar dari drawable ke list tanpa .toString()
        listImage.apply {
            add(ImageSliderData(R.drawable.image1_slider_merchant))
            add(ImageSliderData(R.drawable.image2_slider_merchant))
            add(ImageSliderData(R.drawable.image3_slider_merchant))
        }

        // Inisialisasi adapter dan set ke ViewPager2
        imageSliderAdapter = ImageSliderAdapter(listImage)
        binding.vpMerchantLogo.adapter = imageSliderAdapter
        circleImage = ArrayList()

        setupCircleIndicator()

        binding.vpMerchantLogo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupSelectedIndicator(position)

                // Restart auto-slide timer when manually swiped
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000) // Interval 3 detik
            }
        })

        // Mulai auto-slide
        sliderHandler.postDelayed(sliderRunnable, 3000) // Interval 3 detik
    }

    private fun setupCircleIndicator() {
        for (i in 0 until listImage.size) {
            val indicator = TextView(requireContext())
            indicator.text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            indicator.textSize = 18f
            indicator.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // Warna default
            circleImage.add(indicator)
            binding.llCircle.addView(indicator)
        }
    }

    private fun setupSelectedIndicator(position: Int) {
        for (i in 0 until listImage.size) {
            circleImage[i].setTextColor(
                if (i == position)
                    ContextCompat.getColor(requireContext(), R.color.blue1) // Warna aktif
                else
                    ContextCompat.getColor(requireContext(), R.color.white) // Warna tidak aktif
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        sliderHandler.removeCallbacks(sliderRunnable)
    }
}
