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
            Text(
                text = "First name",
                textAlign = TextAlign.Left,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            TextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end=24.dp, top = 4.dp),
                isError = isFirstNameError
            )
            if (isFirstNameError) {
                Text(
                    text = "Please enter a valid First Name",
                    color = Color.Red,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                )
            }
            Text(
                text = "Last name",
                textAlign = TextAlign.Left,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 12.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            TextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end=24.dp),
                isError = isLastNameError
            )
            if (isLastNameError) {
                Text(
                    text = "Please enter a valid First Name",
                    color = Color.Red,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                )
            }
            EmailTextField(
                email = email,
                onEmailChanged = { email = it },
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
fun EmailTextField(
    email: String,
    onEmailChanged: (String) -> Unit,
    isError: Boolean
) {
    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end=24.dp, bottom = 12.dp, top = 12.dp)) {
        Text(
            text = "Email",
            textAlign = TextAlign.Left,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        TextField(
            value = email,
            onValueChange = {
                onEmailChanged(it)
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError
        )
        if (isError) {
            Text(
                text = "Please enter a valid email address",
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