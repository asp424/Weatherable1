package com.example.weatherable.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.ui.cells.BackgroundImage
import com.example.weatherable.ui.cells.Chelyabinsk
import com.example.weatherable.ui.cells.MyCity
import com.example.weatherable.ui.cells.RealWeather
import com.example.weatherable.ui.cells.SeaCites
import com.example.weatherable.ui.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.json.JSONObject


@Composable
fun WeatherScreen(viewModel: MainViewModel) {
    BackgroundImage()
    val values by remember(viewModel) { viewModel.internetValues }.collectAsState()
    val refreshing by viewModel.internetValuesRefr.collectAsState()
    var t by remember { mutableStateOf(0) }

    LaunchedEffect(refreshing) {
        if (refreshing) viewModel.fetchData()
        else t++
    }
    Text(text = t.toString(), fontSize = 10.sp)
    when (values) {
        is InternetResponse.OnSuccess -> {
            (values as InternetResponse.OnSuccess).dataValues.apply {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = { viewModel.fetchData() },
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            fade = true,
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true,
                            backgroundColor = Blue,
                            contentColor = White,
                            shape = MaterialTheme.shapes.small,
                        )
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
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
                }
            }
        }

        is InternetResponse.Loading -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = White)
            }
            LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)
        }

        else -> {}
    }
    RealWeather(viewModel)
}