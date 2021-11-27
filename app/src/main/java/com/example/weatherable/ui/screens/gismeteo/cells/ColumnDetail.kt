package com.example.weatherable.ui.screens.gismeteo.cells

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.weatherable.R
import com.example.weatherable.ui.cells.Logo


@Composable
fun ColumnDetail(
    listTod: MutableList<String>,
    listTom: MutableList<String>,
    listSkyTod: MutableList<String>,
    listSkyTom: MutableList<String>
) {
    Column {
        ColumnDetailCellGis(header = "Сегодня", inRow1 = {
            listTod.takeLast(8).take(4).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTod[i], item, 0..2, i)
            }
        }) {
            listTod.takeLast(4).forEachIndexed { i, item ->
                CardDetailGis(
                    i, listSkyTod[4 + i], item, 2..3, 4 + i
                )
            }
        }
        ColumnDetailCellGis(header = "Завтра", inRow1 = {
            listTom.takeLast(8).take(4).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTom[i], item, 0..2, i)
            }
        }, inRow2 = {
            listTom.takeLast(4).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTom[4 + i], item, 2..3, 4 + i)
            }
        })
        Logo(R.drawable.gismeteo_logo)
    }
}