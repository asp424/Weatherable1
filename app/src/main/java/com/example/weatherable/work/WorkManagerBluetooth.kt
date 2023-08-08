package com.example.weatherable.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherable.NotificationProvider
import com.example.weatherable.application.appComponent
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.view_states.BluetoothResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class WorkManagerBluetooth(
    appContext: Context, workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var notificationProvider: NotificationProvider

    init {
        appContext.applicationContext.appComponent.inject(this)
    }

    private var job: Job = Job()

    override fun doWork(): Result {
        if (job.isActive) job.cancel()
        job = collector()
        notificationProvider.notification()
        return Result.success()
    }

    private fun collector() =
        CoroutineScope(Dispatchers.IO).launch {
           /* repository.getBluetoothData("a").collect {
                when (it) {
                    is BluetoothResponse.OnSuccess -> {
                        delay(2000L)
                        job.cancel()
                    }
                    is BluetoothResponse.Error -> {
                        job.cancel()
                    }
                    else -> {}
                }
            }

            */
        }
}
