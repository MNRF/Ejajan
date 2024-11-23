package com.mnrf.ejajan.view.main.merchant.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.merchantWelcome.text = getString(R.string.welcome_merchant)
        binding.merchantDeskripsi.text = getString(R.string.deskripsi_homeMerchant)
        binding.saldoText.text = getString(R.string.saldo_rp_10_000)
        binding.statusPesananTitle.text = getString(R.string.status_pesanan_aktif)

        binding.seeAllButton.setOnClickListener {
            // Tambahkan logika di sini untuk aksi tombol
        }

        // Anda juga dapat menambahkan RecyclerView di sini jika diperlukan
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
