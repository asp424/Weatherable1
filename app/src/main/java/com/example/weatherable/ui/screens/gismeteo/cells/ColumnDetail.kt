package com.example.weatherable.ui.screens.gismeteo.cells

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.weatherable.R
import com.example.weatherable.ui.cells.Logo
import com.example.weatherable.utilites.log


@Composable
fun ColumnDetail(
    listTod: MutableList<String>,
    listTom: MutableList<String>,
    listSkyTod: MutableList<String>,
    listSkyTom: MutableList<String>
) {
    listSkyTom.log
    Column {
        ColumnDetailCellGis(header = "Сегодня", inRow1 = {
            listTod.subList(7, 11).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTod[i].substringBefore("\" data-kind=\"Obs"),
                    item, 0..2, i)
            }
        }) {
            listTod.subList(11, 15).forEachIndexed { i, item ->
                CardDetailGis(
                    i, listSkyTod[4 + i].substringBefore("\" data-kind=\"Obs"), item,
                    2..3, 4 + i
                )
            }
        }
        ColumnDetailCellGis(header = "Завтра", inRow1 = {
            listTom.subList(7, 11).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTom[i].substringBefore("\" data-kind=\"Frc"),
                    item, 0..2, i)
            }
        }, inRow2 = {
            listTom.subList(11, 15).forEachIndexed { i, item ->
                CardDetailGis(i, listSkyTom[4 + i].substringBefore("\" data-kind=\"Frc"),
                    item, 2..3, 4 + i)
            }
        })
        Logo(R.drawable.gismeteo_logo, 0)
    }
}