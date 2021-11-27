package com.example.weatherable.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.*
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.data.view_states.InternetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository) :
    ViewModel(),
    LifecycleObserver {
    private val _internetValues: MutableStateFlow<InternetResponse?> =
        MutableStateFlow(InternetResponse.Loading)
    val internetValues = _internetValues.asStateFlow()
    private val _bluetoothValues: MutableStateFlow<BluetoothResponse?> =
        MutableStateFlow(BluetoothResponse.Start)
    val bluetoothValues = _bluetoothValues.asStateFlow()
    private val _pointsList: MutableStateFlow<List<Offset>?> =
        MutableStateFlow(emptyList())
    val pointsList = _pointsList.asStateFlow()
    private val _presList: MutableStateFlow<List<PressureModel>?> =
        MutableStateFlow(emptyList())
    val presList = _presList.asStateFlow()
    private var intJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.M)
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getJsoupData() {
        _bluetoothValues.value = BluetoothResponse.Start
        intJob?.cancel()
        intJob = viewModelScope.launch {
            repository.getJsoupData().collect {
                _internetValues.value = it
            }
        }
        intJob?.start()
    }

    private var blueJob: Job? = null
    fun getBluetoothValues() {
        _bluetoothValues.value = BluetoothResponse.Loading
        blueJob = viewModelScope.launch {
            repository.getBluetoothData().collect {
                _bluetoothValues.value = it
            }
        }
        blueJob?.start()
    }

    fun stop() {
        _bluetoothValues.value = BluetoothResponse.Wait
        intJob?.cancel()
        blueJob?.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopBluetooth() {
        _bluetoothValues.value = BluetoothResponse.Start
        intJob?.cancel()
        blueJob?.cancel()
    }

    fun getTempsForTable(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTemps()?.forEach {

            }
        }
    }

    fun getPresForTable(){
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getAllPressure()!!.isNotEmpty())
            {
                val oX = repository.getAllPressure()?.get(0)?.id?.toFloat()
                val oY = repository.getAllPressure()?.get(0)?.pressure?.toFloat()!!
                val listPoints = mutableListOf<Offset>()
                _presList.value = repository.getAllPressure()
                repository.getAllPressure()?.forEach {
                    listPoints.add(Offset(x = ((((it.id.toFloat()) - oX!!)/10000) + 110f)/4 ,
                        y = -(it.pressure?.toFloat()!! - oY) * 100 + 1920f))
                    if (listPoints.size == repository.getAllPressure()?.size)
                        _pointsList.value = listPoints
                }
            }
        }
    }

    fun clearTablesValues(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearPressureList()
            repository.clearTempsList()
        }
    }
}