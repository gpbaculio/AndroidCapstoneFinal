package com.example.littlelemon

object FilterHelper {
    fun filterProducts(categories: List<String>, menuItems: List<MenuItemRoom>): List<MenuItemRoom> {
        return menuItems.filter { it.category in categories }
    }
}