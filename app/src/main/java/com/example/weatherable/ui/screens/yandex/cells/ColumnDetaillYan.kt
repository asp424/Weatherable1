package com.example.weatherable.ui.screens.yandex.cells

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import com.example.weatherable.R
import com.example.weatherable.ui.cells.Header
import com.example.weatherable.ui.cells.Logo
import com.example.weatherable.ui.cells.RowCards
import org.json.JSONObject

@Composable
fun ColumnDetailYan(
    json: JSONObject
) {
    val listHour = listOf("Утром", "Днём", "Вечером", "Ночью")
    json.apply {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Header(string = "Сегодня", color = Black)
            RowCards {
                for (i in 0..3) {
                    CardDetailYan(
                        time = listHour[i],
                        rain = getString("yan_temp_rain$i"),
                        index = i, temp = getString("yan_temp_tod$i")
                    )
                }
            }
            Header(string = "Завтра", color = Black)
            RowCards {
                for (i in 4..7) {
                    CardDetailYan(
                        time = listHour[i - 4],
                        rain = getString("yan_temp_rain_t$i"),
                        index = i - 4, temp = getString("yan_temp_tom$i")
                    )}
            }
            Logo(R.drawable.yan_logo, 1)
        }
    }
}