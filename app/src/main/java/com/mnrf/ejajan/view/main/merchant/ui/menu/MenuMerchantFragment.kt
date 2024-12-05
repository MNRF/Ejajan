package com.mnrf.ejajan.view.main.merchant.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnrf.ejajan.databinding.FragmentMerchantMenuBinding
import com.mnrf.ejajan.view.main.merchant.ui.menu.add.AddMenuActivity
import com.mnrf.ejajan.view.utils.ViewModelFactory

class MenuMerchantFragment : Fragment() {

    private var _binding: FragmentMerchantMenuBinding? = null
    private val binding get() = _binding!!

    private val menuMerchantViewModel: MenuMerchantViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: MenuListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        menuMerchantViewModel = ViewModelProvider(this)[com.mnrf.ejajan.view.main.merchant.ui.menu.MenuMerchantViewModel::class.java]*/
        adapter = MenuListAdapter()

        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMenu.adapter = adapter

        // Observe the menu list
        menuMerchantViewModel.menuList.observe(viewLifecycleOwner) { menuList ->
            adapter.submitList(menuList)
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), AddMenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
