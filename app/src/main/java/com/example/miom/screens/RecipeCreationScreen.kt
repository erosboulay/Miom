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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
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
    var recipeDescription by remember { mutableStateOf(TextFieldValue("")) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val nameFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }

    val listState = rememberLazyListState()

    val onAddRecipe = {
        val imageName = saveImageToInternalStorage(context, selectedImageUri.value)
        Recipes.addRecipe(recipeName, recipeDescription.text, imageName)
        onBack()
    }

    MiomTheme {
        BackHandler { onBack() }

        Scaffold(
            containerColor = GreyLighter,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = onAddRecipe) {
                            Icon(Icons.Default.Done, null)
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
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                    NameTextField(
                        hint = "Recipe name",
                        modifier = Modifier.focusRequester(nameFocusRequester),
                        style = Typography.titleLarge,
                        text = recipeName,
                        onValueChange = { recipeName = it },
                        onNext = {
                            descriptionFocusRequester.requestFocus()
                        }
                    )

                    AddImagePicker(selectedImageUri)

                    DescriptionTextField(
                        hint = "Start typing your recipe",
                        modifier = Modifier
                            .focusRequester(descriptionFocusRequester),
                        style = Typography.bodyLarge,
                        text = recipeDescription,
                        onValueChange = { recipeDescription = it }
                    )
            }
        }
    }

    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    LaunchedEffect(recipeDescription.text.length) {
        if (recipeDescription.selection.collapsed &&
            recipeDescription.selection.start == recipeDescription.text.length &&
            recipeDescription.text.isNotEmpty()
        ) {
            listState.animateScrollToItem(3)
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
fun NameTextField(
    hint: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    text: String,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = style,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(hint, style = style, color = GreyDark)
            }
            innerTextField()
        }
    )

}

@Composable
fun DescriptionTextField(
    hint: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    text: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable{}
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxSize(),
            textStyle = style,
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
                    Text(hint, style = style, color = GreyDark)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun AddImagePicker(selectedImageUri: MutableState<Uri?>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }

    if (selectedImageUri.value == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, GreyDarkest), RoundedCornerShape(15.dp))
                .clickable { launcher.launch("image/*") }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Upload, null, tint = GreyDarkest)
                Spacer(Modifier.width(10.dp))
                Text("Add Photo", color = GreyDarkest)
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(15.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri.value),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .clickable { selectedImageUri.value = null },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DeleteOutline, null, tint = Color.White)
            }
        }
    }
}
