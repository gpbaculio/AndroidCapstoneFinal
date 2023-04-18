package com.example.littlelemon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


interface Destinations {
    val route: String
    val icon: ImageVector
    val title: String
}

object Home : Destinations {
    override val route = "Home"
    override val icon = Icons.Default.Home
    override val title = "Home"
}

object Profile : Destinations {
    override val route = "Profile"
    override val icon = Icons.Default.Person
    override val title = "Profile"
}


