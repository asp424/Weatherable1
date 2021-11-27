package com.example.weatherable.ui.cells

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherable.utilites.isOnline

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Loading(context: Context) {
    Column(
        Modifier
            .wrapContentSize()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (isOnline(context)) {
                    Header(
                        string = "Загрузка...",
                        paddingTop = 8.dp, paddingStart = 8.dp,
                        paddingBottom = 8.dp, paddingEnd = 8.dp,
                        color = Color.Black
                    )
                } else {
                    Header(
                        string = "Отсутствует интернет",
                        color = Color.Red,
                        paddingTop = 8.dp, paddingStart = 8.dp,
                        paddingBottom = 8.dp, paddingEnd = 8.dp
                    )
                }
            }
        }
    }
}