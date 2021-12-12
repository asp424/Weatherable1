package com.example.weatherable.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.ui.cells.Header
import com.example.weatherable.utilites.getCity
import com.example.weatherable.utilites.setCity

@Composable
fun Settings() {
    val context = LocalContext.current
    val listCity = listOf("Челябинск", "Пушкин", "Москва", "Крымск")
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) { Header(string = "Город", color = Black) }
        var s by remember { mutableStateOf(0) }
        Text(text = s.toString(), fontSize = 1.sp)
        listCity.forEach { city ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = city, modifier = Modifier.padding(bottom = 6.dp), fontSize = 16.sp)
                Checkbox(checked = getCity(context) == city,
                    onCheckedChange = {
                        s++
                        setCity(context, city)
                    })
            }
        }
    }
}