package com.example.littlelemon


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.request.get
import androidx.compose.runtime.*
import io.ktor.client.call.*
import io.ktor.client.request.*

@Composable
fun HomeScreen() {
    var data by remember { mutableStateOf(emptyList<MenuItemNetwork>()) }

    val image = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_IMAGE, SharedPreferencesManager.image)

    LaunchedEffect(Unit) {
       val result = HttpClient
           .httpClient
           .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json ")
           .body<MenuNetwork>()
           .menu

        data = result
    }



    Column() {
        Text(text = "HomeScreen $image")
        if(image.isNotEmpty()) {
            DisplayImage(image)
        }
    }
}