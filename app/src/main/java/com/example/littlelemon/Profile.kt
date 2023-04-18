package com.example.littlelemon



import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    val image = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_IMAGE, SharedPreferencesManager.image)
    val initialFirstName = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_FIRSTNAME, SharedPreferencesManager.firstName)
    val initialLastName = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_LASTNAME, SharedPreferencesManager.lastName)
    val initialEmail = SharedPreferencesManager.getString(SharedPreferencesManager.PRF_KEY_EMAIL, SharedPreferencesManager.email)

    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }
    var email by remember { mutableStateOf(initialEmail) }
    var isEmailError by remember { mutableStateOf(false) }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }



    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        SharedPreferencesManager.saveString(SharedPreferencesManager.PRF_KEY_IMAGE, uri.toString())

    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        }
    }



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

    Button(
        onClick = {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                pickImageLauncher.launch("image/*")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    ) {
        Text(
            text = if(image.isNotEmpty()) {
                "Change"
            } else {
                "Select Image from Gallery"
            }
        )
    }

        if (image.isNotEmpty()) {
            DisplayImage(image )
        }

        if(image.isEmpty()){
            Text(text = "No image selected", modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp))
        }

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayImage(imageUri: String) {
    val imageUriParsed = Uri.parse(imageUri)
    GlideImage(
        model = imageUriParsed,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}