package com.example.weatherable.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.ui.cells.*
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.example.weatherable.utilites.isOnline
import org.json.JSONObject


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WeatherScreen(
    viewModel: MainViewModel
) {
    var visible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current as MainActivity
    BackgroundImage()
    val values by remember(viewModel) {
        viewModel.internetValues
    }.collectAsState()
    when (values) {
        is InternetResponse.OnSuccess -> {
            (values as InternetResponse.OnSuccess).dataValues.apply {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {
                    Visibility(visible = visible) {
                        Card(backgroundColor = Color.Yellow) {
                            Header(
                                string = "Отсутствует интернет",
                                color = Color.Red,
                                paddingTop = 8.dp, paddingStart = 8.dp,
                                paddingBottom = 8.dp, paddingEnd = 8.dp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 65.dp, top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyCity(
                            JSONObject()
                                .put("yan_temp", getString("yan_temp"))
                                .put("hydro_temp", getString("hydro_temp"))
                                .put("gis_temp", getString("gis_temp"))
                                .put("krm_wind", getString("krm_wind"))
                        )
                        Chelyabinsk(getString("chel_temp"))
                    }
                    SeaCites(
                        JSONObject()
                            .put("nov_value", getJSONObject("nov_value"))
                            .put("ana_value", getJSONObject("ana_value"))
                            .put("gel_value", getJSONObject("gel_value"))
                    )
                }
                RealWeather(viewModel)
            }
        }
        is InternetResponse.Loading -> {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isOnline(context)) {
                    Card(backgroundColor = Color.Black) {
                        Header(
                            string = "Загрузка...",
                            paddingTop = 8.dp, paddingStart = 8.dp,
                            paddingBottom = 8.dp, paddingEnd = 8.dp
                        )
                    }
                } else {
                    visible = true
                    Card(backgroundColor = Color.Yellow) {
                        Header(
                            string = "Отсутствует интернет",
                            color = Color.Red,
                            paddingTop = 8.dp, paddingStart = 8.dp,
                            paddingBottom = 8.dp, paddingEnd = 8.dp
                        )
                    }
                }

            }
            LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)
        }
    }
}