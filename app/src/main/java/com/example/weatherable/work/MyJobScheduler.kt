package com.example.weatherable.work

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.weatherable.application.appComponent
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.view_states.BluetoothResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MyJobScheduler : JobService() {

    private var job: Job = Job()

    @Inject
    lateinit var repository: Repository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (job.isActive) job.cancel()
        //job = CoroutineScope(IO).launch { repository.getBluetoothData().collect(::collector) }
        return true
    }


    private suspend fun collector(res: BluetoothResponse) {
        when (res) {
            is BluetoothResponse.OnSuccess -> {
                delay(2000L)
                onStopJob(null)
            }
            is BluetoothResponse.Error -> {
                onStopJob(null)
            }
            else -> {}
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        job.cancel()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
