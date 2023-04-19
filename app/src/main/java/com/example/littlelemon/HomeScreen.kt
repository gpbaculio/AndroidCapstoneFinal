package com.example.littlelemon
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.material.TextFieldColors
import android.graphics.drawable.Drawable
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumptech.glide.request.target.Target
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }
    var isLoading by remember { mutableStateOf(false) }
    val image = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_IMAGE, SharedPreferencesManager.image)
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    var menuItems = databaseMenuItems
    val menuCategories = menuItems.map { it.category }.distinct()
    val dao = database.menuItemDao()

    LaunchedEffect(Unit) {
        val isDaoEmpty = withContext(Dispatchers.IO) {
            dao.isEmpty()
        }
        if (isDaoEmpty) {
            isLoading = true
            val menuItemsNetwork = HttpClient
                .httpClient
                .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json ")
                .body<MenuNetwork>()
                .menu
            withContext(Dispatchers.IO) {
                val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
                dao.insertAll(*menuItemsRoom.toTypedArray())
            }
            isLoading = false
        }
    }


    Column() {
        if(image.isNotEmpty()) {
            DisplayImage(image)
        }
        if(isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressLoader()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .background(Color(0xFF495E57))
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Little Lemon",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF4CE14)
                )
                Text(
                    text = "Chicago",
                    fontSize = 24.sp,
                    color = Color(0xFFEDEFEE)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 18.dp, bottom = 18.dp)
                        .height(140.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                        color = Color(0xFFEDEFEE),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(bottom = 28.dp)
                            .fillMaxWidth(0.6f),
                    )
                    GlideImage(
                        model =  R.drawable.upperpanelimage,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    )
                }

                var searchPhrase by remember {
                    mutableStateOf("")
                }

                Box(
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                ) {
                    TextField(
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF495E57)
                            )
                        },
                        value = searchPhrase,
                        onValueChange = {
                            searchPhrase = it
                        },
                        label = { Text("Search") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                        ,

                    )
                }

                if (searchPhrase.isNotEmpty()) {
                    menuItems = menuItems.filter { it.title.contains(searchPhrase, ignoreCase = true) }
                }
            }
            Text(
                text = "ORDER FOR DELIVERY!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyRow {
                items(menuCategories) { category ->
                    MenuCategory(category)
                }
            }
            Divider(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
            LazyColumn {
                items(menuItems) { Menu ->
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

@Composable
fun MenuCategory(category: String) {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
        shape = RoundedCornerShape(40),
        modifier = Modifier.padding(5.dp)
    ) {
        Text(
            text = category
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuDish(Menu: MenuItemRoom) {
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
            .background(
                if (isLoading) {
                    Color.LightGray
                } else {
                    Color.White
                }
            )
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
