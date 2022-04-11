package com.example.weatherable.ui.cells

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
import com.example.weatherable.utilites.repPlus
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
    val coroutine = rememberCoroutineScope()
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


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RealWeather(viewModel: MainViewModel) {
    val values by remember(viewModel) {
        viewModel.bluetoothValues
    }.collectAsState()
    var visible by remember {
        mutableStateOf(false)
    }
    var visibleCard by remember {
        mutableStateOf(false)
    }
    val listValuesNames =
        mutableListOf(stringResource(id = R.string.temp), stringResource(id = R.string.press))
    var rotation by remember { mutableStateOf(0f) }
    val coroutine = rememberCoroutineScope()
    val coroutine1 = rememberCoroutineScope()
    val coroutine2 = rememberCoroutineScope()
    var scale by remember { mutableStateOf(1f) }
    var scale1 by remember { mutableStateOf(1f) }
    var enable by remember { mutableStateOf(false) }
    (LocalContext.current as MainActivity).also { cont ->
        var job: Job? by remember {
            mutableStateOf(null)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(end = 15.dp, bottom = 70.dp)
                .fillMaxSize()
        ) {
            Card(border = BorderStroke(2.dp, Black)) {
                Icon(Icons.Outlined.AutoGraph, contentDescription = null,
                    Modifier
                        .clickable {
                            visibleCard = !visibleCard
                            viewModel.getPresForTable()
                        }
                        .size(40.dp))
            }
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(start = 15.dp, bottom = 70.dp)
                .fillMaxSize()
        ) {
            Card(border = BorderStroke(2.dp, Black), modifier = Modifier.padding(end = 6.dp)) {
                Icon(Icons.Default.Timer, contentDescription = null,
                    Modifier
                        .clickable {
                            viewModel.runOneTimeWork()
                            viewModel.runPeriodicWork()
                            Toast
                                .makeText(cont, "start", Toast.LENGTH_SHORT)
                                .show()

                        }
                        .size(40.dp)
                )
            }

            Card(border = BorderStroke(2.dp, Black)) {
                Icon(Icons.Default.TimerOff, contentDescription = null,
                    Modifier
                        .clickable {
                            viewModel.stopWork()
                            Toast
                                .makeText(cont, "stop", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .size(40.dp)
                )
            }
        }

        Column(
            Modifier
                .padding(bottom = 80.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedButton(
                onClick = {
                    job?.cancel()
                    visible = false
                    if (rotation == 0f) {
                        enable = true
                        job = coroutine2.launch {
                            while (true) {
                                delay(1L)
                                rotation += 0.7f
                            }
                        }
                        job?.start()
                        viewModel.getBluetoothValues()
                    } else {
                        enable = false
                        viewModel.stopBluetooth()
                        rotation = 0f
                        job?.cancel()
                    }
                }, Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        scaleY = scale1
                        scaleX = scale1
                    }
            ) {
                Icon(
                    Icons.Outlined.Bluetooth,
                    contentDescription = null,
                    Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                )
            }
            Visibility(visible = visible) {
                Card(backgroundColor = Color.White) {
                    Header(
                        string = "Неудачная попытка",
                        paddingTop = 8.dp, paddingStart = 8.dp,
                        paddingBottom = 8.dp, paddingEnd = 8.dp, color = Red
                    )
                }
            }
        }

        Row(
            Modifier
                .padding(bottom = 155.dp)
                .fillMaxSize()
                .graphicsLayer {
                    scaleY = scale
                    scaleX = scale
                },
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .clickable(enabled = enable) {
                        enable = false
                        viewModel.stop()
                    },
                elevation = 10.dp,
                backgroundColor = colorResource(id = R.color.full_green)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Header(string = stringResource(id = R.string.blue_name))
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(Modifier.padding(start = 20.dp)) {
                            listValuesNames.forEach {
                                Name(string = it)
                            }
                        }
                        Column(modifier = Modifier.padding(start = 5.dp)) {
                            var temp by remember {
                                mutableStateOf("")
                            }
                            var pres by remember {
                                mutableStateOf("")
                            }
                            Value(string = temp)
                            Value(string = pres)
                            when (values) {
                                is BluetoothResponse.OnSuccess -> {
                                    viewModel.getPresForTable()
                                    job?.cancel()
                                    coroutine1.launch {
                                        while (scale1 != -0.009999329f) {
                                            delay(5L)
                                            scale1 -= 0.01f
                                            if (scale1 == -0.009999329f) {
                                                rotation = 0f
                                                while (scale != 1.0099994f) {
                                                    delay(5L)
                                                    scale += 0.01f
                                                }
                                                break
                                            }
                                        }
                                    }
                                }
                                is BluetoothResponse.Temp -> {
                                    temp = (values as BluetoothResponse.Temp).temp
                                }
                                is BluetoothResponse.Press -> {
                                    pres = (values as BluetoothResponse.Press).press
                                }
                                is BluetoothResponse.Loading -> {

                                }
                                is BluetoothResponse.Wait -> {
                                    coroutine.launch {
                                        while (scale != -0.009999925f) {
                                            delay(5L)
                                            scale -= 0.01f
                                            if (scale == -0.009999925f) {
                                                while (scale1 != 1f) {
                                                    delay(5L)
                                                    scale1 += 0.01f
                                                }
                                                break
                                            }
                                        }
                                    }
                                }
                                is BluetoothResponse.Error -> {
                                    LaunchedEffect(BluetoothResponse.Error("")) {
                                        visible = true
                                        delay(2500L)
                                        visible = false
                                    }
                                    rotation = 0f
                                    job?.cancel()
                                }

                                is BluetoothResponse.Start -> {
                                    enable = false
                                    job?.cancel()
                                    rotation = 0f
                                    scale = 0f
                                    scale1 = 1f
                                }
                            }
                        }
                    }
                }
            }

        }
        Table(visible = visibleCard, viewModel = viewModel)
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
                    listOnSeaNames.forEach {
                        Name(string = it)
                    }
                }
                dataCity.apply {
                    Column(modifier = Modifier.padding(start = 5.dp)) {
                        Value(string = getJSONObject("nov_value").getString("temp"))
                        Value(string = getJSONObject("ana_value").getString("temp"))
                        Value(string = getJSONObject("gel_value").getString("temp"))
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        WaterName()
                        WaterName()
                        WaterName()
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Value(
                            string = getJSONObject("nov_value").getString("water"),
                            color = Color.Blue
                        )
                        Value(
                            string = getJSONObject("ana_value").getString("water"),
                            color = Color.Blue
                        )
                        Value(
                            string = getJSONObject("gel_value").getString("water"),
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    }
}
