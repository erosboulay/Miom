package com.example.miom.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.miom.database.Images.Companion.saveImageToInternalStorage
import com.example.miom.database.Recipes
import com.example.miom.ui.theme.GreyDark
import com.example.miom.ui.theme.GreyDarkest
import com.example.miom.ui.theme.GreyLighter
import com.example.miom.ui.theme.MiomTheme
import com.example.miom.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCreationScreen(onBack: () -> Unit) {

    var recipeName by remember { mutableStateOf("") }
    var recipeDescription by remember { mutableStateOf("") }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current


    val onAddRecipe = {
        // Now you can access everything!
        println("Saving Recipe: $recipeName")
        println("Description: $recipeDescription")
        println("Image: $selectedImageUri")

        val imageName = saveImageToInternalStorage(context, selectedImageUri.value)
        Recipes.addRecipe(recipeName, recipeDescription, imageName)

        onBack() // Go back after saving
    }

    MiomTheme {
        // Handles the system back button
        BackHandler { onBack() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = GreyLighter,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onAddRecipe) {  // <-- Add this
                            Icon(
                                imageVector = Icons.Default.Done,  // Done icon
                                contentDescription = "Done"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GreyLighter
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp) // Consistent with your app's padding
                    .fillMaxSize()
            ) {
                val focusRequester = remember { FocusRequester() }
                val keyboardController = LocalSoftwareKeyboardController.current

                CustomTextField(
                    "Recipe name",
                    Modifier
                        .padding(0.dp, 0.dp)
                        .focusRequester(focusRequester),
                    Typography.titleLarge,
                    recipeName,
                    onValueChange = { recipeName = it })
                HorizontalDivider(color = Color.Transparent, thickness = 15.dp)

                AddImagePicker(selectedImageUri)
                HorizontalDivider(color = Color.Transparent, thickness = 15.dp)

                CustomTextField(
                    "Start typing your recipe",
                    Modifier.fillMaxSize(),
                    Typography.bodyLarge,
                    recipeDescription,
                    onValueChange = { recipeDescription = it })

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
            }
        }
    }
}



@PreviewScreenSizes
@Composable
fun RecipeCreationScreenPreview() {
    MiomTheme {
        RecipeCreationScreen(onBack = {})
    }
}

@Composable
fun CustomTextField(hint: String, modifier: Modifier = Modifier, style: TextStyle, text: String, onValueChange: (String) -> Unit) {

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        textStyle = style,
        decorationBox = { innerTextField ->
            // Manual placeholder implementation
            if (text.isEmpty()) {
                Text(
                    text = hint,
                    style = style,
                    color = GreyDark// Customize your hint color
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun AddImageBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(BorderStroke(1.dp, GreyDarkest), shape = RoundedCornerShape(15.dp))
            .clickable { onClick() }, // handle click
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Upload,
                contentDescription = "Add Photo",
                tint = GreyDarkest,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Add Photo", fontSize = 16.sp, color = GreyDarkest)
        }
    }
}

@Composable
fun AddImagePicker(selectedImageUri: MutableState<Uri?>) {
    // Launcher for picking an image
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Called when user selects an image
        selectedImageUri.value = uri
    }

    if (selectedImageUri.value == null){
        AddImageBox(onClick = {
            launcher.launch("image/*") // Open image picker for images
        })
    }
    else{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(15.dp))
        ) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri.value),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Delete icon with semi-transparent background and margin
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)           // position top-right
                    .padding(12.dp)                     // margin from edges
                    .size(32.dp)                        // size of the circle
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .clickable { selectedImageUri.value = null },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)      // size of the icon inside the circle
                )
            }
        }

    }

}