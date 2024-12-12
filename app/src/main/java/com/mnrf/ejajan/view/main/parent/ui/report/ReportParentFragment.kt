package com.mnrf.ejajan.view.main.parent.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.mnrf.ejajan.databinding.FragmentParentReportBinding
import com.mnrf.ejajan.view.main.parent.ui.adapter.HistoryAdapter
import com.mnrf.ejajan.view.utils.ViewModelFactory

class ReportParentFragment : Fragment() {

    private var _binding: FragmentParentReportBinding? = null
    private val binding get() = _binding!!

    private val reportViewModel: ReportViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        setupRecyclerView()
        reportViewModel.orderList.observe(viewLifecycleOwner) { orderList ->
            historyAdapter.submitList(orderList)
        }

    }

//    private fun setupPieChart(pieChart: PieChart) {
//        pieChart.description.isEnabled = false
//        pieChart.isDrawHoleEnabled = true
//        pieChart.setUsePercentValues(true)
//        pieChart.setEntryLabelTextSize(12f)
//        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
//    }
//
//    private fun setupBarChart(barChart: BarChart) {
//        // Contoh pengaturan BarChart
//        barChart.description.isEnabled = false
//        barChart.setFitBars(true)
//    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
