package com.mnrf.ejajan.view.main.parent.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
        setupBarChart()

        reportViewModel.orderList.observe(viewLifecycleOwner) { orderList ->
            historyAdapter.submitList(orderList)
        }
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun setupBarChart() {
        val barChart = binding.barChart

        // Dummy data untuk Bar Chart
        val entries = listOf(
            BarEntry(1f, 10000f), // Senin
            BarEntry(2f, 7000f),  // Selasa
            BarEntry(3f, 15000f), // Rabu
            BarEntry(4f, 10000f), // Kamis
            BarEntry(5f, 15000f)  // Jumat
        )
        val dataSet = BarDataSet(entries, "Weekly Spending").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 12f
        }

        val barData = BarData(dataSet)
        barChart.data = barData

        // Set X-Axis Labels
        val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(days)
            granularity = 1f
            textSize = 12f
            position = XAxis.XAxisPosition.BOTTOM
        }

        // Styling Bar Chart
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.granularity = 5000f
        barChart.axisLeft.axisMaximum = 20000f // Sesuaikan untuk nilai tertinggi
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()

        // Hitung Total Pengeluaran
        val totalSpending = entries.sumOf { it.y.toDouble() }
        displaySummary(totalSpending)
    }

    private fun displaySummary(totalSpending: Double) {
        val summaryText = buildString {
            append("Total pengeluaran minggu ini: Rp%.0f".format(totalSpending))
            append("\n")
            if (totalSpending > 50000) {
                append("Pengeluaran Anak anda cukup besar, pertimbangkan untuk mengurangi pembelian makanan/minuman di beberapa hari.")
            } else {
                append("Pengeluaran Anak anda masih dalam batas wajar. Tetap pertahankan kebiasaan pembelian makanan/minuman ini.")
            }
        }
        binding.tvSummary.text = summaryText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
