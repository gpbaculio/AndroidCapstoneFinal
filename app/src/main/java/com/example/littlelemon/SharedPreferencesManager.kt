package com.example.littlelemon

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf

object SharedPreferencesManager {
    var PRF_KEY_FIRSTNAME = "firstname"
    var PRF_KEY_LASTNAME = "lastname"
    var PRF_KEY_EMAIL = "email"
    var PRF_KEY_IMAGE = "image"
    private const val PREFERENCES_NAME = "AppPreferences"
    private lateinit var sharedPreferences: SharedPreferences
    private val _firstName = mutableStateOf("")
    val firstName: String
        get() = _firstName.value
    private val _lastName = mutableStateOf("")
    val lastName: String
        get() = _lastName.value
    private val _email = mutableStateOf("")
    val email: String
        get() = _email.value
    private val _image = mutableStateOf("")
    val image: String
        get() = _image.value

    // Initialize SharedPreferences with the application context
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // Function to store a string value in SharedPreferences
    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        if(key === PRF_KEY_FIRSTNAME) {
            _firstName.value = value
        }
        if(key === PRF_KEY_LASTNAME) {
            _lastName.value = value
        }
        if(key === PRF_KEY_EMAIL) {
            _email.value = value
        }
        if(key === PRF_KEY_IMAGE) {
            _image.value = value
        }
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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
