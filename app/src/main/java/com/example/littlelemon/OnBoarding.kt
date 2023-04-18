package com.example.littlelemon
 
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun OnBoarding() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    )   {

        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var isEmailError by remember { mutableStateOf(false) }
        var isFirstNameError by remember { mutableStateOf(false) }
        var isLastNameError by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )   {
            Image(
                painter = painterResource(id = R.drawable.littlelemonimgtxt_nobg),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier
                    .fillMaxWidth(.40f)
                    .padding(16.dp),
            )
            Text(
                text = "Let's get to know you",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Green)
                    .padding(36.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
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
                    .padding(bottom = 24.dp, start = 24.dp,end=24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        isFirstNameError = !validateStringWithCapitalAtFirstAndAfterSpace(firstName)
                        isLastNameError = !validateStringWithCapitalAtFirstAndAfterSpace(lastName)
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
}

fun isValidEmail(email: String): Boolean {
    // Regular expression pattern for email validation
    val emailPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")

    // Use the matches() function to check if the email matches the pattern
    return email.matches(emailPattern)
}

fun validateStringWithCapitalAtFirstAndAfterSpace(inputString: String): Boolean {
    // Accepts a capital letter at the first index of the string, after every space,
    // and ensures every space is followed or preceded by at least 2 characters
    val regex = Regex("(?i)^(?:[A-Z][a-z]*\\w{1}\\s+)*[A-Z][a-z]*\\w{1}$")

    return inputString.matches(regex) && inputString.isNotEmpty()
}

@Composable
fun FormField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end=24.dp, bottom = 12.dp, top = 12.dp)
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Left,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        TextField(
            value = value,
            onValueChange = {
                onChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError
        )

        if (isError) {
            val errorMessage = if (label == "Email") "Please enter valid Email Address" else "Please enter a valid $label. Must begin with Capital."
            Text(
                text = errorMessage,
                color = Color.Red,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    OnBoarding()
}