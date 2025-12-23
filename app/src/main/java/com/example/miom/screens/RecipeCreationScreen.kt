package com.example.miom.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.miom.ui.theme.GreyDark
import com.example.miom.ui.theme.GreyLighter
import com.example.miom.ui.theme.MiomTheme
import com.example.miom.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCreationScreen(onBack: () -> Unit) {

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
                CustomTextField("Recipe name", Modifier.padding(0.dp, 0.dp))

                // Your blank recipe form goes here
                Text(
                    text = "New recipe form details...",
                    modifier = Modifier.padding(top = 16.dp)
                )
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
fun CustomTextField(hint: String, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = Typography.titleLarge,
        decorationBox = { innerTextField ->
            // Manual placeholder implementation
            if (text.isEmpty()) {
                Text(
                    text = hint,
                    style = Typography.titleLarge,
                    color = GreyDark// Customize your hint color
                )
            }
            innerTextField()
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}
