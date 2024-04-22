package com.example.horizontalbarchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lineChart: LineChart = findViewById(R.id.lineChart)

        // 데이터 생성 (임의의 데이터 예시)
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 10f))
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 15f))
        // ... 더 많은 데이터 추가

        // 데이터셋 생성
        val dataSet = LineDataSet(entries, "My Data")
        dataSet.color = resources.getColor(R.color.colorPrimary)
        dataSet.valueTextColor = resources.getColor(R.color.colorAccent)

        // LineData 생성
        val lineData = LineData(dataSet)

        // 차트 설정
        lineChart.data = lineData
        lineChart.description.text = "My Line Chart"
        // ... 다른 설정 추가

        // 차트 업데이트
        lineChart.invalidate()
    }
}