package com.mnrf.ejajan.view.main.parent.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.mnrf.ejajan.databinding.FragmentParentReportBinding

class ReportParentFragment : Fragment() {

    private var _binding: FragmentParentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[ReportViewModel::class.java]
        _binding = FragmentParentReportBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPieChart(binding.pieChart)
        setupBarChart(binding.barChart)
        setupRecyclerView()

    }

    private fun setupPieChart(pieChart: PieChart) {
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
    }

    private fun setupBarChart(barChart: BarChart) {
        // Contoh pengaturan BarChart
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
    }

    private fun setupRecyclerView() {
        // Contoh konfigurasi RecyclerView
        binding.rvHistory.apply {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
