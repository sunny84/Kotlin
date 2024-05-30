@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}
@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {
    // Compose는 State를 기반으로 동작을 한다.
    val text1: MutableState<String> = remember {
        mutableStateOf("Hello World")
    }
    var text2 by remember {
        mutableStateOf("Hello World")
    }
    val (text: String, setText: (String) -> Unit) = remember {
        mutableStateOf("Hello World")
    }

    val text3: State<String> = viewModel.liveData.observeAsState("Hello World")

    Column () {
        Text("Hello World")
        Button(onClick = {
            text1.value = "변경"
            print(text1.value)
            text2 = "변경"
            print(text2)
            setText("변경")
            //viewModel.value.value = "변경"
            viewModel.changeValue("변경")
        }) {
            Text("클릭")
        }
        TextField(value = text, onValueChange = setText)
    }
}
class MainViewModel: ViewModel() {
    // mutableStateOf : 읽기, 쓰기가 가능한 타입
    private val _value : MutableState<String> = mutableStateOf("Hello World")
    val value: State<String> = _value // State : 읽기만 가능한 타입

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData
    fun changeValue(value: String){
        _value.value = value
    }
}
//class MainViewModel : ViewModel(){
//    private val _data = mutableStateOf("Hello")
//    val data: State<String> = _data
//
//    fun changeValue() {
//        _data.value = "World"
//    }
//}
@Composable
fun FirstScreen(navController: NavController) {
    val (value, setValue) = remember {
        mutableStateOf("")
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "첫 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("second")
        }) {
            Text("두 번째")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = value, onValueChange = setValue)
        Button(onClick = {
            if (value.isNotEmpty()){
                navController.navigate("third/$value")
            }
        }) {
            Text("세 번째")
        }
    }
}
@Composable
fun SecondScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "두 번째 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigateUp()
            //navController.popBackStack()
        }) {
            Text("뒤로 가기!")
        }
    }
}
@Composable
fun ThirdScreen(navController: NavController, value: String ) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "세 번째 화면")
        Spacer(modifier = Modifier.height(16.dp))
        Text(value)
        Button(onClick = {
            navController.navigateUp()
        }) {
            Text("뒤로 가기!")
        }
    }
}