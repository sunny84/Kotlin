package com.example.linechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linechart.ui.theme.LineChartTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.HorizontalLegend
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.shape.Shape


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
                    //Greeting("Android")
                    LineChartExample()
                }
            }
        }
    }
}

@Composable
fun ColumnChartExample01() {
    val data = mapOf("A" to 8f, "B" to 4f, "C" to 6f)
    val labelListKey = ExtraStore.Key<List<String>>()
    val cartesianChartModelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        cartesianChartModelProducer.tryRunTransaction {
            columnSeries { series(data.values) }
            updateExtras { it[labelListKey] = data.keys.toList() }
        }
    }
    CartesianValueFormatter { x, chartValues, _ -> chartValues.model.extraStore[labelListKey][x.toInt()] }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        ),
        cartesianChartModelProducer,
    )
}
@Composable
fun rememberLegend(colors: List<Color>): Legend<CartesianMeasureContext, CartesianDrawContext>? {
    val labelTextList = listOf("A", "B", "C")
    return HorizontalLegend(
        items = List(labelTextList.size) { index ->
            LegendItem(
                icon = ShapeComponent(
                    shape = Shape.Pill,
                    color = colors[index].value.toInt() // TODO: 이해 안됨
                ),
                label = TextComponent.build(),
                labelText = labelTextList[index]
            )
        },
        iconSizeDp = 10.dp.value,
        iconPaddingDp = 8.dp.value,
        spacingDp = 10.dp.value,
        padding = Dimensions.of(top = 8.dp)
    )
}
@Composable
fun LineChartExample() {
    val colorList = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta
    )
    val data = mapOf(
        LocalDate.parse("2022-07-01") to 2f,
        LocalDate.parse("2022-07-02") to 6f,
        LocalDate.parse("2022-07-04") to 4f,
    )
    val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()
    val cartesianChartModelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
        cartesianChartModelProducer.tryRunTransaction {
            lineSeries { series(xToDates.keys, data.values) }
            updateExtras { it[xToDateMapKey] = xToDates }
        }
    }
    val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
    CartesianValueFormatter { x, chartValues, _ ->
        (chartValues.model.extraStore[xToDateMapKey][x] ?: LocalDate.ofEpochDay(x.toLong())).format(dateTimeFormatter)
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            legend = rememberLegend(colors = colorList),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
            //chartScrollState = rememberChartScrollState()
        ),
        cartesianChartModelProducer,
    )
}
@Preview
@Composable
fun ColumnChartPreview() {
    LineChartExample()
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // CartesianChartModelProducer를 생성합니다. 이는 차트 데이터를 비동기적으로 처리하는 데 사용됩니다.
    // remember 를 사용하여 인스턴스를 기억합니다.
    val modelProducer = remember { CartesianChartModelProducer.build() }
    // LaunchedEffect 를 사용하여 비동기 트랜잭션을 실행합니다.
    // lineSeries 를 추가하여 꺾은선 그래프의 데이터를 설정합니다.
    LaunchedEffect("Graph1") {
        modelProducer.tryRunTransaction {
            // 4채널에 대한 그래프를 추가합니다.
            lineSeries { series(4, 12, 8, 16) }
        }
    }

    LaunchedEffect("Graph2") {
        modelProducer.tryRunTransaction {
            // 9채널에 대한 그래프를 추가합니다.
            lineSeries { series(3, 6, 9, 12) }
        }
    }

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
        //TestVicoChart()
    }
}
