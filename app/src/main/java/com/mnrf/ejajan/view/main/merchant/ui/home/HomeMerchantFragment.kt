package com.mnrf.ejajan.view.main.merchant.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentMerchantHomeBinding
import com.mnrf.ejajan.view.main.merchant.ui.activeorder.OrderListActivity

class HomeMerchantFragment : Fragment() {

    private var _binding: FragmentMerchantHomeBinding? = null

    private val binding get() = _binding!!

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

        binding.tvMerchantWelcome.text = getString(R.string.welcome_merchant)
        binding.tvMerchantDeskripsi.text = getString(R.string.deskripsi_homeMerchant)
        binding.tvSaldo.text = getString(R.string.saldo_rp_10_000)
        binding.tvStatusPesanan.text = getString(R.string.status_pesanan_aktif)

        binding.tvSeeAll.setOnClickListener {
            // Tambahkan logika di sini untuk aksi tombol
            val intent = Intent(requireContext(), OrderListActivity::class.java)
            startActivity(intent)
        }

        // Anda juga dapat menambahkan RecyclerView di sini jika diperlukan
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
