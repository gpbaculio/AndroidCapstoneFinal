package com.example.littlelemon


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    val image = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_IMAGE, SharedPreferencesManager.image)
    
    Column() {
        Text(text = "HomeScreen $image")
        if(image.isNotEmpty()) {
            DisplayImage(image)
        }
    }
}