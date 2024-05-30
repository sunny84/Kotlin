@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 구조분해
            val (text, setValue) = remember {
                mutableStateOf("")
            }
            // M2 ScaffoldState 클래스는 더 이상 필요하지 않은 drawerState 매개변수를 포함하므로 더 이상 M3에 없습니다.
            // M3 Scaffold로 스낵바를 표시하려면 대신 SnackbarHostState를 사용합니다.
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val keyboardController = LocalSoftwareKeyboardController.current

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                content = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(value = text, onValueChange = setValue)
                        Button(onClick = {
                            keyboardController?.hide()
                            scope.launch { snackbarHostState.showSnackbar("Hello $text") }
                        }) {
                            Text("클릭!!")
                        }
                    }
                }
            )
            //MyApp {
//                MainContent()
            //}
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    // 2022.2.17 Code
    MyApplicationTheme {
        Scaffold ( topBar = {
            TopAppBar(
                title = { Text(text = "Title text") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite"
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            )
            }, bottomBar = {
                BottomAppBar(
                    content = {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        Text(text = "BottomAppBar!")
                    }
                )
            }, floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                content = {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite")
                }
            )
// M2 Scaffold의 모든 drawer* 매개변수가 M3 Scaffold에서 삭제되었습니다.
//            }, drawerContent = {
//                Icon(
//                    modifier = Modifier.padding(14.dp),
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Person"
//                )
//                Text(modifier = Modifier.padding(14.dp), text = "James")
//                Text(modifier = Modifier.padding(14.dp), text = "Sam")
//                Text(modifier = Modifier.padding(14.dp), text = "Jon")
            },
            content = {
                LazyColumn {}
            }
        )
    }
}

@Composable
fun MainContent() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Blue)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = "Hello")
            Text(text = "World")
        }
    }
}

@Composable
fun MyApplication() {
    MyApplicationTheme {
        // Surface
        // 주로 단일 요소의 배경 모양, 색, 테두리 등을 설정하는 Composable이다.
        // Surface를 사용해야 하는 이유는 코드를 더욱 쉽게 만들고, Material Surface를 사용함을 명시적으로 나타낸다.

        // Scaffold
        // 앱의 기본적인 레이아웃 구조를 정의하고 다양한 UI 구성 요소를 통합하는 데 사용된다.
        // 일반적으로 앱의 최상위 컨테이너 역할을 하고 다음과 같은 주요 구성 요소로 구성될 수 있다.
        // TopAppBar, BottomAppBar, FloatingActionButton, Drawer, Content
        // Scaffold를 사용하면 위의 구성 요소를 조합하여 앱의 기본 레이아웃을 구성하고, 표준화된 Material Design 스타일을 적용할 수 있다.

        // Modifier
        // Compose의 수정자인 Modifier는 Composable를 꾸미거나 더욱 강조한다.

        Surface() {
            Column(
                modifier = Modifier
                    .background(color = Color.Blue)
                    .padding(16.dp)
            ) {
                Text("Hello")
                Text("World")
            }
        }
    }
}