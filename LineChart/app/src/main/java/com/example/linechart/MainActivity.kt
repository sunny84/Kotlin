package com.example.linechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.linechart.ui.theme.LineChartTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LineChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // CartesianChartModelProducer를 생성합니다. 이는 차트 데이터를 비동기적으로 처리하는 데 사용됩니다.
    // remember 를 사용하여 인스턴스를 기업합니다.
    val modelProducer = remember { CartesianChartModelProducer.build() }
    // LaunchedEffect 를 사용하여 비동기 트랜잭션을 실행합니다.
    // lineSeries 를 추가하여 꺾은선 그래프의 데이터를 설정합니다.
    LaunchedEffect(Unit) { modelProducer.tryRunTransaction { lineSeries { series(4, 12, 8, 16) } } }
    // CartesianChartHost 를 사용하여 차트를 호스팅한다.
    // rememberCartesianChart 를 사용하여 차트를 생성하고, startAxis와 bottomAxis 를 설정합니다.
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        modelProducer,
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LineChartTheme {
        Greeting("Android")
    }
}
