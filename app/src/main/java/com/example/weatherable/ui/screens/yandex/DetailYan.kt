package com.example.weatherable.ui.screens.yandex

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.example.weatherable.activity.DetailYanActivity
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.ui.cells.DetailCard
import com.example.weatherable.ui.cells.Loading
import com.example.weatherable.ui.screens.yandex.cells.ColumnDetailYan
import com.example.weatherable.ui.viewmodel.DetailYanViewModel


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun DetailYan(viewModel: DetailYanViewModel) {
    val values by remember(viewModel) { viewModel.internetValues }.collectAsState()
    val context = LocalContext.current as DetailYanActivity
    val lifeCycle = LocalLifecycleOwner.current.lifecycle

    when (values) {
        is InternetResponse.OnSuccess -> {
            DetailCard {
                ColumnDetailYan((values as InternetResponse.OnSuccess).dataValues)
            }
        }
        is InternetResponse.Loading -> Loading(context)
        else -> {
        }
    }
    LaunchedEffect(lifeCycle) { lifeCycle.addObserver(viewModel) }
    DisposableEffect(lifeCycle) {
        onDispose { lifeCycle.removeObserver(viewModel) }
    }
}



