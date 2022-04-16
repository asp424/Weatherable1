package com.example.weatherable.ui.viewmodel

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobInfo.NETWORK_TYPE_ANY
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.*
import androidx.work.*
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.data.view_states.InternetResponse
import com.example.weatherable.work.MyJobScheduler
import com.example.weatherable.work.WorkManagerBluetooth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val workManager: WorkManager
) :
    ViewModel(),
    LifecycleObserver {
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
    private var intJob: Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getJsoupData() {
        _bluetoothValues.value = BluetoothResponse.Start
        getInternetValues()
    }

    fun getInternetValues() {
        _internetValuesRefr.value = true
        intJob?.cancel()
        intJob = viewModelScope.launch {
            repository.getJsoupData().collect {
                _internetValues.value = it
                _internetValuesRefr.value = false
            }
        }
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

    fun getTempsForTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTemps()?.forEach {

            }
        }
    }

    fun getPresForTable() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getAllPressure().isNotEmpty())
                _presList.value = repository.getAllPressure()
        }
    }

    fun clearTablesValues() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearPressureList()
            repository.clearTempsList()
        }
    }

    fun checkService(mainActivity: MainActivity) =
        (mainActivity.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler).allPendingJobs.size == 0


    fun scheduleJob(mainActivity: MainActivity) =
        (mainActivity.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler).apply {
            if (allPendingJobs.size == 0)
                schedule(
                    JobInfo.Builder(123, ComponentName(mainActivity, MyJobScheduler::class.java))
                        .setRequiresCharging(false).setRequiredNetworkType(NETWORK_TYPE_ANY)
                        .setPersisted(true).setPeriodic(60 * 60 * 1000L).build()
                )
            else cancelAll()
        }

    fun runPeriodicWork() = workManager.enqueueUniquePeriodicWork(
        "per", ExistingPeriodicWorkPolicy.KEEP,
        PeriodicWorkRequestBuilder<WorkManagerBluetooth>(
            15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES
        ).build()
    )

    fun runOneTimeWork() = workManager.enqueueUniqueWork(
        "one",
        ExistingWorkPolicy.KEEP,
        OneTimeWorkRequestBuilder<WorkManagerBluetooth>().build()
    )

    fun stateWork() = Log.d("My", workManager.getWorkInfosByTag("a").toString())

    fun stopWork() = workManager.cancelAllWork()

}
