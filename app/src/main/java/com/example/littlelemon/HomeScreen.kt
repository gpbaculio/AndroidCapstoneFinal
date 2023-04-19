package com.example.littlelemon


import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.ktor.client.call.*
import io.ktor.client.request.*

@Composable
fun HomeScreen() {
    var data by remember { mutableStateOf(emptyList<MenuItemNetwork>()) }

    var isLoading by remember { mutableStateOf(false) }
    val image = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_IMAGE, SharedPreferencesManager.image)

    LaunchedEffect(Unit) {
        isLoading = true
        val result = HttpClient
           .httpClient
           .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json ")
           .body<MenuNetwork>()
           .menu
        isLoading = false
        data = result
    }



    Column() {
        if(image.isNotEmpty()) {
            DisplayImage(image)
        }
        if(isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...")
            }
        } else {
            LazyColumn {
                items(data) { Menu ->
                    MenuDish(Menu)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(Menu: MenuItemNetwork) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = Menu.title, fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    text = Menu.description,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth(.75f)
                )
                Text(
                    text = "${Menu.price}", color = Color.Gray, fontWeight = FontWeight.Bold
                )
            }
            GlideImage(
                model = Menu.image,
                contentDescription = Menu.description,
                contentScale = ContentScale.FillWidth, 
                requestBuilderTransform = {
                    it.override(768, 768)
                        .error(R.drawable.image)
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image)
                }
            )
        }
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = Color.LightGray,
        thickness = 1.dp
    )
}
