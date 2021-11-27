package com.example.weatherable.ui.cells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.weatherable.R

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
}