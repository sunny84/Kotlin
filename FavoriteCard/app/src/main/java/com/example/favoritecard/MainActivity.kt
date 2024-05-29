package com.example.favoritecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.favoritecard.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            var isFavorite by rememberSaveable {
//                mutableStateOf(false)
//            }
//            ImageCard(
//                modifier = Modifier
//                    .fillMaxWidth(0.5f)
//                    .padding(16.dp),
//                isFavorite = isFavorite,
//            ) { favorite ->
//                isFavorite = favorite
//            }
            var isFavorite by rememberSaveable {
                mutableStateOf(false)
            }
            Column {
                FavoriteCard1()
                FavoriteCard2()
//                FavoriteCard3(
//                    modifier = Modifier.height(200.dp),
//                    isFavorite = isFavorite
//                ) { favorite ->
//                   isFavorite = favorite
//                }
                Box(
                    modifier = Modifier.fillMaxSize(.3f),
                    contentAlignment = Alignment.Center
                ) {
                    FavoriteCard(
                        isFavorite = isFavorite
                    ) {
                        isFavorite = !isFavorite
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(
    modifier: Modifier = Modifier, // 재사용을 위해 외부 지정으로 수정
    isFavorite: Boolean, // <- 변수가 아니라 상수이다.
    onTabFavorite: (Boolean) -> Unit,
) {
// 재사용 가능하도록 수정
//    // isFavorite.value 를 사용하지 않고 isFavorite 를 Boolean 타입으로 사용하려면
//    // 수동으로 remember.getValue, remember.setValue를 import 해주고 by remember를 사용합니다.
//    // 화면 회전시에도 isFavorite 값이 초기화되지 않게 하기 위해서는 rememberSaveable 를 사용한다.
//    var isFavorite by rememberSaveable {
//        mutableStateOf(false)
//    }

    Card(
        // 재사용하려면 내부 modifier을 외부 지정으로 수정
//        modifier = Modifier
//            .fillMaxWidth(0.5f)
//            .padding(16.dp),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(
            modifier = Modifier.height(200.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.poster),
                contentDescription = "poster",
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd,
            ) {
                IconButton(onClick = {
                    // isFavorite = !isFavorite // <- 상수 값으로 바꿀 수 없음
                    // 콜백을 사용해야 한다.
                    // onTabFavorite.invoke(!isFavorite) 에서 .invoke는 생략 가능
                    onTabFavorite(!isFavorite)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCard1() {
    // 델리게이트 프로퍼티를 사용하면 value를 사용하지 않아도 된다.
    val isFavorite = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize(.3f),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = {
            isFavorite.value = !isFavorite.value
        }) {
            Icon(
                imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "favorite",
                tint = if (isFavorite.value) Color.Red else Color.Gray
            )
        }
    }
}

@Composable
fun FavoriteCard2() {
    // Delegated properties(위임된 속성)를 사용하면 value를 사용하지 않아도 됩니다.
    // by 뒤의 표현식은 속성에 해당하는 get() (및 set())이 getValue() 및 setValue() 메서드에 위임되기 때문에 delegate(대리자)입니다.
    var isFavorite by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize(.3f),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = {
            isFavorite = !isFavorite
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "favorite",
                tint = if (isFavorite) Color.Blue else Color.Gray
            )
        }
    }
}

@Composable
fun FavoriteCard3(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onTabFavorite: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(.3f),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = {
            onTabFavorite.invoke(!isFavorite)
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "favorite",
                tint = Color.Green,
            )
        }
    }
}

@Composable
fun FavoriteCard(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    // UI 구현
    Card(
        modifier = modifier
            .clickable { onToggleFavorite() }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) Color.Green else Color.Gray
        )
    }
}