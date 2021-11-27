package com.example.weatherable.ui.cells

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weatherable.R

@Composable
fun WaterName(
    string: String = stringResource(id = R.string.water_name),
    color: Color = Color.Blue,
    paddingEnd: Dp = 0.dp,
    paddingTop: Dp = 3.dp,
    paddingStart: Dp = 7.dp,
) {
    Text(
        text = string,
        fontFamily = fontsFamily,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(start = paddingStart, top = paddingTop, end = paddingEnd),
        style = TextStyle(color = color)
    )
}