package com.example.devmobile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    // Retrieve saved values or defaults
    var firstName by remember {
        mutableStateOf(sharedPreferences.getString("firstName", "") ?: "")
    }
    var lastName by remember {
        mutableStateOf(sharedPreferences.getString("lastName", "") ?: "")
    }
    var selectedSport by remember { mutableStateOf("Football") } // Default value
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val sports = listOf("Football", "Basketball", "Tennis", "Volleyball", "Swimming")

    // Profile Picture
//    var imageUri by remember {
//        mutableStateOf(
//            sharedPreferences.getString("imageUri", null)?.toUri()
//        )
//    }
//    val bitmap =  remember { mutableStateOf<Bitmap?>(null) }

    // Gallery
//    val launcher = rememberLauncherForActivityResult(contract =
//        ActivityResultContracts.GetContent()) { uri: Uri? ->
//        imageUri = uri
//    }
//
//    // Camera
//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicturePreview(),
//        onResult = {
//            bitmap.value = it
//        }
//    )
    // Save data when firstName or lastName changes
    LaunchedEffect(firstName, lastName) {
        with(sharedPreferences.edit()) {
            putString("firstName", firstName)
            putString("lastName", lastName)
            apply()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {

            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                imageUri?.let {
//                    if (bitmap.value == null){
//                        val file = File.createTempFile("file", ".jpg")
//                        file.deleteOnExit()
//                        val newFileUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
//
//                        val resolver = context.contentResolver
//                        val inputStream = resolver.openInputStream(it)
//                        val outputStream = resolver.openOutputStream(newFileUri)
//                        inputStream?.copyTo(outputStream!!)
//                        imageUri = newFileUri
//
//                        bitmap.value = if (Build.VERSION.SDK_INT < 28) {
//                            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//                        } else {
//                            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
//                            ImageDecoder.decodeBitmap(source)
//                        }
//                    }
//
//                    if (bitmap.value != null){
//                        Image(
//                            bitmap = bitmap.value!!.asImageBitmap(),
//                            contentDescription = "Profile Picture",
//                            modifier = Modifier
//                                .size(120.dp)
//                                .clip(CircleShape),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//                }
//                if(imageUri == null && bitmap.value == null){
                    Image(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//                Row {
//                    Button(onClick = {
//                        launcher.launch("image/*")
//                    }) {
//                        Text("Gallery")
//                    }
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Button(onClick = {
//                        cameraLauncher.launch(null)
//                    }) {
//                        Text("Camera")
//                    }
//                }
//            }
//            Column(modifier = Modifier.weight(1f)) {
//
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Favorite Sport Dropdown
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.clickable { isDropdownExpanded = !isDropdownExpanded }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Favorite Sport: $selectedSport", Modifier.padding(16.dp))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                }
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                sports.forEach { sport ->
                    DropdownMenuItem(
                        text = { Text(sport) },
                        onClick = {
                            selectedSport = sport
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}