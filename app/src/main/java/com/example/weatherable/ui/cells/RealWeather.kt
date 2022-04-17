package com.example.weatherable.ui.cells

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherable.R
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
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
            Card(border = BorderStroke(2.dp, Color.Black)) {
                Icon(
                    Icons.Outlined.AutoGraph, contentDescription = null,
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
            Card(border = BorderStroke(2.dp, Color.Black), modifier = Modifier.padding(end = 6.dp)) {
                Icon(
                    Icons.Default.Timer, contentDescription = null,
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

            Card(border = BorderStroke(2.dp, Color.Black)) {
                Icon(
                    Icons.Default.TimerOff, contentDescription = null,
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
                        paddingBottom = 8.dp, paddingEnd = 8.dp, color = Color.Red
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
    (LocalContext.current as MainActivity).apply {
        BackHandler {
            if (visibleCard) visibleCard = false else finish()
        }
    }
}