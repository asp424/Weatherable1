package com.example.weatherable.ui.cells

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowCards(inRow: @Composable (RowScope) -> Unit){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        inRow(this)
    }
}