package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BusinessCardApp()
                }
            }
        }
    }
}

@Composable
fun BusinessCard(imagePainter: Painter, name: String, title: String, phone: String, instagram: String, email: String, modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xffaaddaa)),
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.weight(1f)
        ) {
            Row {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier
                        .background(Color(0xff000055))
                        .size(80.dp)
                )
            }
            Row {
                Text(
                    text = name,
                    fontSize = 32.sp
                )
            }
            Row(
                modifier = modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF115511),
                )
            }
        }
        Column (
            //verticalAlignment = Alignment.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(bottom = 32.dp) // 간격 조정
        ){
            Row {
                Icon(
                    Icons.Rounded.Phone,
                    contentDescription = null,
                    tint = Color(0xFF115511),
                    modifier = modifier.padding(end = 10.dp)
                )
                Text(
                    text = phone,
                    fontSize = 11.sp,
                    modifier = modifier.padding(top = 4.dp)
                )
            }
            Row {
                Icon(
                    Icons.Rounded.Share,
                    contentDescription = null,
                    tint = Color(0xFF115511),
                    modifier = modifier.padding(end = 10.dp)
                )
                Text(
                    text = instagram,
                    fontSize = 11.sp,
                    modifier = modifier.padding(top = 4.dp)
                )
            }
            Row {
                Icon(
                    Icons.Rounded.Email,
                    contentDescription = null,
                    tint = Color(0xFF115511),
                    modifier = modifier.padding(end = 10.dp)
                )
                Text(
                    text = email,
                    fontSize = 11.sp,
                    modifier = modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun BusinessCardApp(){
    BusinessCard(
        imagePainter = painterResource(id = R.drawable.android_logo),
        name = "Full Name",
        title = "Title",
        phone = "+11 (123) 444 555 666", //"+00 (00) 000 000",
        instagram = "@socialmediahandle",
        email = "email@domail.com"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BusinessCardTheme {
        BusinessCardApp()
    }
}