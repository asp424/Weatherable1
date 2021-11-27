package com.example.weatherable.ui.cells

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    string: String,
    fontSize: TextUnit = 20.sp,
    color: Color = Color.White,
    paddingEnd: Dp = 0.dp, paddingTop: Dp = 6.dp,
    paddingStart: Dp = 0.dp, paddingBottom: Dp = 0.dp,
    backGroundColor: Color = Color.Transparent
) {
    Text(
        text = string,
        fontSize = fontSize,
        style = TextStyle(
            color = color,
            shadow = Shadow(
                color = Color.DarkGray,
                blurRadius = 5.5f,
                offset = Offset(7.0F, 9.0f)
            ), background = backGroundColor
        ),
        modifier = Modifier.padding(
            top = paddingTop,
            bottom = paddingBottom,
            start = paddingStart,
            end = paddingEnd
        )
    )
}