package com.example.weatherable.ui.cells

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Name(string: String?, color: Color = Color.Black) {
    if (string != null) {
        Text(
            text = string,
            fontFamily = fontsFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(color)
        )
    }
}