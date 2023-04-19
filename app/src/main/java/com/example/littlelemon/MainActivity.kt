package com.example.littlelemon

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.ui.theme.LittleLemonTheme
import android.view.Window
import android.view.WindowManager
import android.graphics.Color.parseColor

class MainActivity : ComponentActivity() {
    private fun setStatusBarColor(color: Int) {
        // Get the window object
        val window: Window = window

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(parseColor("#495E57"))
        SharedPreferencesManager.init(this)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    LittleLemonTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            val firstName=SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_FIRSTNAME, SharedPreferencesManager.firstName)
            val lastName=SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_LASTNAME, SharedPreferencesManager.lastName)
            val email=SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_EMAIL, SharedPreferencesManager.email)

            if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty()) {
                OnBoarding()
            } else {
                LoggedInUser()
            }
        }
    }
}

@Composable
fun LoggedInUser() {
    val navController = rememberNavController()
    val selectedIndex = rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(bottomBar = { BottomNavigation(navController = navController,selectedIndex) }) {
        Box(Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Home.route) {
                composable(Home.route) {
                    HomeScreen(onProfileClick = {  selectedIndex.value = 1
                        navController.navigate("Profile")})
                }
                composable(Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}



@Composable
fun BottomNavigation(navController: NavController, selectedIndex: MutableState<Int>) {
    val destinationList = listOf(
        Home,
        Profile
    )

    BottomNavigation {
        destinationList.forEachIndexed { index, destination ->
            BottomNavigationItem(
                label = { Text(text = destination.title) },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.title
                    )
                },
                selected = index == selectedIndex.value,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(destinationList[index].route) {
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }
                })
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}