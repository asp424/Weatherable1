package com.example.weatherable.ui.screens.yandex.cells

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.weatherable.utilites.getIconDayYan
import com.example.weatherable.utilites.getIconNightYan

@Composable
fun CardDetailYan(time: String, rain: String, index: Int, temp: String){
    Card(
        modifier = Modifier.padding(3.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.width(60.dp)

        ) {
            Text(text = time,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
            Image(
                painter = rememberImagePainter(
                    if (index == 0 || index == 1) getIconDayYan(rain)
                    else getIconNightYan(rain)),
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )
            Text(text = temp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp)
                )
        }
    }
}