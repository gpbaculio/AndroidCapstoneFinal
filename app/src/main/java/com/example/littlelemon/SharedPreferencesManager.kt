package com.example.littlelemon

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    var PRF_KEY_FIRSTNAME = "firstname"
    var PRF_KEY_LASTNAME = "lastname"
    var PRF_KEY_EMAIL = "email"
    private const val PREFERENCES_NAME = "AppPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize SharedPreferences with the application context
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // Function to store a boolean value in SharedPreferences
    fun saveBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // Function to retrieve a boolean value from SharedPreferences
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Function to store a string value in SharedPreferences
    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Function to retrieve a string value from SharedPreferences
    fun getString(key: String, defaultValue: String): String  {
        return sharedPreferences.getString(key, null) ?: defaultValue
    }

    // Function to remove a key-value pair from SharedPreferences
    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    // Function to clear all data from SharedPreferences
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
