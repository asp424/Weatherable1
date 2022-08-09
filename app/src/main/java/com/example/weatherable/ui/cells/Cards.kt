package com.example.weatherable.ui.cells

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.R
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.log
import com.example.weatherable.utilites.rep
import com.example.weatherable.utilites.repD
import com.example.weatherable.utilites.repPlus
import com.google.gson.JsonObject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject


// WeatherScreen's
@Composable
fun MyCity(dataMyCity: JSONObject) {
    val listSitesNames = mutableListOf(
        stringResource(id = R.string.yandex_name), stringResource(id = R.string.hydromet_name),
        stringResource(id = R.string.gismeteo_name)
    )
    Card(
        modifier = Modifier.padding(start = 10.dp, end = 20.dp),
        elevation = 10.dp,
        backgroundColor = colorResource(id = R.color.back)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(stringResource(id = R.string.krymsk_name))
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    listSitesNames.forEach {
                        Name(string = it)
                    }
                }
                Column(modifier = Modifier.padding(start = 5.dp)) {
                    Value(string = dataMyCity.getString("yan_temp"))
                    Value(string = dataMyCity.getString("hydro_temp"))
                    Value(string = dataMyCity.getString("gis_temp"))

                }
            }
            Row(horizontalArrangement = Arrangement.Start) {
                WaterName(string = "ветер", paddingEnd = 8.dp, paddingTop = 2.dp)
                Value(
                    string = dataMyCity.getString("krm_wind"),
                    color = Color.Blue,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun Chelyabinsk(chelTemp: String) {
    Card(
        modifier = Modifier.padding(10.dp),
        elevation = 10.dp,
        backgroundColor = colorResource(id = R.color.light)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Header(string = stringResource(id = R.string.chel_name), fontSize = 20.sp)
            Box(modifier = Modifier.padding(top = 10.dp)) {
                Value(string = chelTemp.repPlus)
            }
        }
    }
}


@Composable
fun SeaCites(dataCity: JSONObject) {
    val listOnSeaNames = mutableListOf(
        stringResource(id = R.string.nov_name),
        stringResource(id = R.string.ana_name),
        stringResource(id = R.string.gel_name)
    )
    Card(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
        elevation = 10.dp,
        backgroundColor = colorResource(id = R.color.background_kur),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(stringResource(id = R.string.kur_name))
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    listOnSeaNames.forEach { Name(string = it) }
                }
                dataCity.apply {
                    Column(modifier = Modifier.padding(start = 5.dp)) {
                        Value(string = temp("nov_value"))
                        Value(string = temp("ana_value"))
                        Value(string = temp("gel_value"))
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        repeat(3) { WaterName() }
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Value(string = water("nov_value").repD, color = Blue)
                        Value(string = water("ana_value").repD, color = Blue)
                        Value(string = water("gel_value").repD, color = Blue)
                    }
                }
            }
        }
    }
}

private fun JSONObject.water(value: String) = getJSONObject(value).getString("water")
private fun JSONObject.temp(value: String) = getJSONObject(value).getString("temp")
