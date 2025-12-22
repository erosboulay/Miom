package com.example.miom.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miom.ui.theme.Typography

@Composable
fun Title(title: String) {
    Text(
        text = title,
        style = Typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp)
    )
}
