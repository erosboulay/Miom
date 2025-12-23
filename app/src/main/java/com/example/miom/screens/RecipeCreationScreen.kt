package com.example.miom.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miom.ui.theme.GreyDark
import com.example.miom.ui.theme.GreyDarkest
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
                val focusRequester = remember { FocusRequester() }
                val keyboardController = LocalSoftwareKeyboardController.current

                CustomTextField(
                    "Recipe name",
                    Modifier
                        .padding(0.dp, 0.dp)
                        .focusRequester(focusRequester),
                    Typography.titleLarge)

                HorizontalDivider(color = Color.Transparent, thickness = 15.dp)

                AddImageBox(){}

                HorizontalDivider(color = Color.Transparent, thickness = 15.dp)

                CustomTextField(
                    "Start typing your recipe",
                    Modifier.fillMaxSize(),
                    Typography.bodyLarge)


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
fun CustomTextField(hint: String, modifier: Modifier = Modifier, style: TextStyle) {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        onValueChange = { text = it },
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
            .background(Color.Transparent)
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