package com.example.weatherable.ui.cells

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DetailCard(inCard: @Composable () -> Unit){
    Column(
        Modifier.wrapContentHeight().fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(16.dp), modifier = Modifier.wrapContentSize()
        ) {
            inCard()
        }
    }
}