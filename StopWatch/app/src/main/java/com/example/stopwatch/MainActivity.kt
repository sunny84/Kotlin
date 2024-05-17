package com.example.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.ui.theme.StopWatchTheme
import kotlinx.coroutines.delay
import java.util.Timer
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopWatchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Stopwatch()
                }
            }
        }
    }
}

@Composable
fun Stopwatch() {
    var expanded by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(0L) } // Time in milliseconds
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while(isRunning) {
                delay(10L)
                time += 10L
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(20.dp)
    ) {
        Row (
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(50.dp)
        ) {
            Text(text = String.format("%02d", (time / 60000) % 60),
                modifier = Modifier.alignByBaseline(),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold)
            Text(text = ":${String.format("%02d", (time / 1000) % 60)}",
                modifier = Modifier.alignByBaseline(),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold)
            Text(text = ".${String.format("%02d", (time / 10) % 100)}",
                modifier = Modifier.alignByBaseline(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {
                expanded = !expanded
                time = 0L
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.filledTonalButtonColors(),
            modifier = Modifier.padding(10.dp)
                .size(width = 150.dp, height = 60.dp)
        ) {
            Text("초기화", fontSize = 20.sp)
        }
        Button(
            onClick = { isRunning = !isRunning },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color.Red else Color.Blue
            ),
            modifier = Modifier
                .padding(10.dp)
                .size(width = 150.dp, height = 60.dp)
        ) {
            Text(if (isRunning) "중지" else "시작", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopWatchTheme {
        Stopwatch()
    }
}