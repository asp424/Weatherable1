package com.example.weatherable.data.repository

import android.content.Context
import com.example.weatherable.data.bluetooth.BluetoothSource
import com.example.weatherable.data.internet.jsoup.JsoupSource
import com.example.weatherable.data.internet.retrofit.RestSource
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.data.view_states.InternetResponse
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject
constructor(
    private val bluetoothSource: BluetoothSource,
    private val jsoupSource: JsoupSource
) {
    suspend fun getJsoupData() = flow { emit(jsoupSource.getCityValues()) }

    @OptIn(InternalCoroutinesApi::class)
    suspend fun getBluetoothData(source: String = "") = bluetoothSource.runBluetooth(source)

    fun getAllTemps() = bluetoothSource.getAllTemps()
    fun getAllPressure() = bluetoothSource.getAllPressure()
    fun clearPressureList() = bluetoothSource.clearPressureList()
    fun clearTempsList() = bluetoothSource.clearTempList()

    suspend fun getGisData(context: Context) = flow { emit(jsoupSource.getGisData(context)) }

    suspend fun getYanData(context: Context) = flow { emit(jsoupSource.getYanData(context)) }
}