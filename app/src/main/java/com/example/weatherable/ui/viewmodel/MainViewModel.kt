package com.example.weatherable.ui.viewmodel

import android.bluetooth.BluetoothSocket
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.work.WorkManagerBluetooth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val repository: Repository
) : ViewModel(), DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner); _bluetoothValues.value = BluetoothResponse.Start; fetchData()
    }

    private val _internetValues: MutableStateFlow<InternetResponse?> =
        MutableStateFlow(InternetResponse.Loading)

    val internetValues = _internetValues.asStateFlow()
    private val _internetValuesRefr: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val internetValuesRefr = _internetValuesRefr.asStateFlow()
    private val _bluetoothValues: MutableStateFlow<BluetoothResponse?> =
        MutableStateFlow(BluetoothResponse.Start)

    val bluetoothValues = _bluetoothValues.asStateFlow()
    private val _pointsList: MutableStateFlow<List<Offset>?> =
        MutableStateFlow(emptyList())

    val pointsList = _pointsList.asStateFlow()
    private val _presList: MutableStateFlow<List<PressureModel>> =
        MutableStateFlow(emptyList())

    val presList = _presList.asStateFlow()
    private val _tempList: MutableStateFlow<List<TempModel>> =
        MutableStateFlow(emptyList())

    val tempList = _tempList.asStateFlow()
    private var intJob: Job? = null

    fun fetchData() {
        _internetValuesRefr.value = true; intJob?.cancel(); intJob = coroutine {
            repository.getJsoupData().collect {
                _internetValues.value = it; _internetValuesRefr.value = false
            }
        }
    }

    private var blueJob: Job? = null
    fun getBluetoothValues() {
        _bluetoothValues.value = BluetoothResponse.Loading; blueJob = coroutine {
            repository.getBluetoothData().collect { _bluetoothValues.value = it }
        }; blueJob?.start()
    }

    fun stop() {
        _bluetoothValues.value = BluetoothResponse.Wait; intJob?.cancel(); blueJob?.cancel()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner); stopBluetooth()
    }

    fun stopBluetooth() {
        _bluetoothValues.value = BluetoothResponse.Start; intJob?.cancel(); blueJob?.cancel()
    }

    fun getTableData() {
        coroutine {
            repository.getAllPressure().collect { if (it.isNotEmpty()) _presList.value = it }
        }; coroutine {
            repository.getAllTemps().collect { if (it.isNotEmpty()) _tempList.value = it }
        }
    }

    fun clearTablesValues() =
        coroutine { with(repository) { clearPressureList(); clearTempsList() } }

    fun runPeriodicWork() = workManager.enqueueUniquePeriodicWork(
        "per", ExistingPeriodicWorkPolicy.KEEP,
        PeriodicWorkRequestBuilder<WorkManagerBluetooth>(
            30, TimeUnit.MINUTES, 5, TimeUnit.MINUTES
        ).build()
    )

    fun runOneTimeWork() = workManager.enqueueUniqueWork(
        "one", ExistingWorkPolicy.KEEP,
        OneTimeWorkRequestBuilder<WorkManagerBluetooth>().build()
    )

    fun stopWork() = workManager.cancelAllWork()

    private fun coroutine(work: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(IO) { work() }

}
