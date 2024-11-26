package com.mnrf.ejajan.view.main.parent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentParentHomeBinding

class HomeParentFragment : Fragment() {

    private var _binding: FragmentParentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan view binding untuk inflating layout
        _binding = FragmentParentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan binding untuk mengakses elemen dalam XML
        binding.tvParentWelcome.text = getString(R.string.welcome_parent)
        binding.ivParentLogo.setImageResource(R.drawable.slider1_merchant)
        binding.tvParentDeskripsi.text = getString(R.string.deskripsi_homeWelcome)
        binding.tvSaldo.text = getString(R.string.saldo_rp_00_000)
        binding.tvTopup.text = getString(R.string.top_up)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // Anda dapat mengatur RecyclerView di sini
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
