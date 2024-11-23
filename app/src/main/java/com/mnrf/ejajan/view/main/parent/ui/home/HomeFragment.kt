package com.mnrf.ejajan.view.main.parent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mnrf.ejajan.R
import com.mnrf.ejajan.databinding.FragmentHome2Binding

class HomeFragment : Fragment() {

    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan view binding untuk inflating layout
        _binding = FragmentHome2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan binding untuk mengakses elemen dalam XML
        binding.parentWelcome.text = getString(R.string.welcome_parent)
        binding.parentLogo.setImageResource(R.drawable.slider1_merchant)
        binding.parentDeskripsi.text = getString(R.string.deskripsi_homeWelcome)
        binding.saldoButton.text = getString(R.string.saldo_rp_00_000)
        binding.topUpButton.text = getString(R.string.top_up)

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
