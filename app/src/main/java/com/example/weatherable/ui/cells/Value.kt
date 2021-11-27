package com.example.weatherable.ui.cells

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.utilites.repPlus

@Composable
fun Value(
    string: String?, color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Medium
) {
    Text(
        text = string!!.repPlus,
        fontSize = 12.sp,
        fontWeight = fontWeight,
        modifier = Modifier
            .padding(2.dp)
            .padding(end = 4.dp, start = 6.dp),

        style = TextStyle(color = color), textAlign = TextAlign.Center
    )
}