package com.example.littlelemon


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    val initialFirstName = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_FIRSTNAME, SharedPreferencesManager.firstName)
    val initialLastName = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_LASTNAME, SharedPreferencesManager.lastName)
    val initialEmail = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_EMAIL, SharedPreferencesManager.email)

    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }
    var email by remember { mutableStateOf(initialEmail) }
    var isEmailError by remember { mutableStateOf(false) }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.littlelemonimgtxt_nobg),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier
                .fillMaxWidth(.40f)
                .padding(16.dp),
        )
        Text(
            text = "Personal Information",
            textAlign = TextAlign.Left,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        FormField(
            label = "First name",
            value = firstName,
            onChange = { firstName = it  },
            isError = isFirstNameError
        )
        FormField(
            label = "Last name",
            value = lastName,
            onChange = { lastName = it  },
            isError = isLastNameError
        )
        FormField(
            label = "Email",
            value = email,
            onChange = { email = it  },
            isError = isEmailError
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    firstName = firstName.trim()
                    lastName = lastName.trim()
                    email = email.trim()
println("firstname $firstName")
                    isFirstNameError = !validFirstAndLastName(firstName)
                    isLastNameError = !validFirstAndLastName(lastName)
                    isEmailError = !isValidEmail(email)

                    if(!isFirstNameError && !isLastNameError && !isEmailError) {
                        SharedPreferencesManager.saveString(SharedPreferencesManager.PRF_KEY_FIRSTNAME, firstName)
                        SharedPreferencesManager.saveString(SharedPreferencesManager.PRF_KEY_LASTNAME, lastName)
                        SharedPreferencesManager.saveString(SharedPreferencesManager.PRF_KEY_EMAIL, email)
                        showToast(context, "Registration successful!")
                    } else {
                        showToast(context, "Registration unsuccessful. Please enter all data.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Click me")
            }
        }
    }
}