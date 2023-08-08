package com.example.weatherable.ui.screens.gismeteo.cells

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
import com.example.weatherable.utilites.getIconDayGis
import com.example.weatherable.utilites.getIconNightGis
import com.example.weatherable.utilites.repPlus

@Composable
fun CardDetailGis(i: Int, image: String, item: String, indexes: IntRange, hour: Int) {
    val min = "00"
    val listHour = listOf("0", "3", "6", "9", "12", "15", "18", "21")
    Card(
        modifier = Modifier.padding(3.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.width(64.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = listHour[hour],
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp)
                )
                Text(text = min, fontSize = 8.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Image(
                painter = rememberImagePainter(
                    if (i in indexes) getIconNightGis(image)
                    else getIconDayGis(image)
                ),
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = image, fontSize = 8.sp, textAlign = TextAlign.Center)
            }
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = item.repPlus,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(text = " °C")
            }
        }
    }
}