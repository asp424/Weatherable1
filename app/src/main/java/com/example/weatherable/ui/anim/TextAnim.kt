package com.example.weatherable.ui.anim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun TextAnim(text: String) {
    var rotation by remember { mutableStateOf(1f) }
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        text.forEachIndexed { index, c ->
            Text(text = c.toString(), modifier = Modifier
                .graphicsLayer { rotationX = rotation + index * 350 }
                .padding(top = 10.dp, bottom = 10.dp),
                style = TextStyle(
                color = Black, shadow = Shadow(
                    color = DarkGray,
                    blurRadius = 5.5f,
                    offset = Offset(7f, 9f)
                ), background = Transparent, fontSize = 24.sp
              )
            )
          }
        LaunchedEffect(true) {
            while(true) {
                delay(1L)
                rotation += 1f
                if (rotation == 350f) rotation = 1f
            }
        }
    }
}

