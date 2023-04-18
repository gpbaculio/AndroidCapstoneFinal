package com.example.littlelemon

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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


            if(SharedPreferencesManager.firstName.isEmpty()
                && SharedPreferencesManager.lastName.isEmpty()
                    && SharedPreferencesManager.email.isEmpty()) {
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
    Scaffold(bottomBar = { BottomNavigation(navController = navController) }) {
        Box(Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Home.route) {
                composable(Home.route) {
                    HomeScreen()
                }
                composable(Profile.route) {
                    ProfileScreen()
                }

            }
        }
    }
}



@Composable
fun BottomNavigation(navController: NavController) {
    val destinationList = listOf(
        Home,
        Profile
    )
    val selectedIndex = rememberSaveable {
        mutableStateOf(1)
    }
    BottomNavigation {
        destinationList.forEachIndexed { index, destination ->
            BottomNavigationItem(
                label = { Text(text = destination.title) },
                icon = {  destination.icon  },
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