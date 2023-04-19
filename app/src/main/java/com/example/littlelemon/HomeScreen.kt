package com.example.littlelemon
import androidx.compose.material.CircularProgressIndicator

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumptech.glide.request.target.Target
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
               Row {
                   CircularProgressLoader()
                   Text(text = "Loading...")
               }
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


@Composable
fun CircularProgressLoader() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp),
        strokeWidth = 4.dp,
        color = MaterialTheme.colors.primary
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(Menu: MenuItemNetwork) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
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
            GlideLoader(Menu.image)

        }
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = Color.LightGray,
        thickness = 1.dp
    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideLoader(image: String) {
    var isLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(if(isLoading) {  Color.LightGray } else {  Color.White  })
        .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        if(isLoading) { CircularProgressLoader() }
        
        GlideImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
            requestBuilderTransform = {
                it
                .override(768, 768)
                .addListener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            isLoading = false
                            // Handle failed loading here
                            return false // Returning false here will allow Glide to propagate the error to any other registered listeners
                        }
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Handle successful loading here
                            isLoading = false
                            return false
                        }
                    }
                )
                .error(R.drawable.image)
            },
        )

    }
}
