package com.example.weatherable.data.bluetooth

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.app.Application
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.weatherable.data.room.bluetooth_db.BluetoothDataDao
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.utilites.DEVICE
import com.example.weatherable.utilites.UUID_VAL
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothSource @Inject constructor(
    private val context: Application,
    private val bD: BluetoothDataDao
) {
    private var socket: BluetoothSocket? = null
    private val device by lazy { adapter.getRemoteDevice(DEVICE) }

    private val adapter by lazy {
        (context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE)
                as BluetoothManager).adapter
    }

    suspend fun runBluetooth(source: String) = callbackFlow {
        launch(Default) { task({ enable }, { task({ close }, { task({ delay(1500L) ; create },
            { task({ work(source) { trySend(it) } }, {}, 1) }) }) }) }
        awaitClose { stop } }.flowOn(IO)

    private inline fun PS.task(run: () -> Unit, suc: () -> Unit, type: Int = 0) =
        runCatching { run() }.onSuccess { suc() }.onFailure { if (type == 1) stop
            trySend(it.error) }

    private inline fun PS.work(source: String, crossinline onSend: (BR) -> Unit) {
        var counter = 0; var writeTemp = true; var writePres = true
        connect; success
        socket!!.inputStream.bufferedReader().use {
            while (isActive) {
                it.readLine().apply {
                    if (check) {
                        when (counter) {
                            0 -> onCycle(writeTemp, { writeTemp(this); writeTemp = false },
                                { onSend(temp); counter = 1 })

                            1 -> onCycle(writePres, { writePress(this, source)
                                writePres = false }, { onSend(press); counter = 0 })
                        }
                    }
                }
            }
        }
    }

    private fun onCycle(write: Boolean, onWrite: () -> Unit, onSend: () -> Unit)
    { if (write) { onWrite() }; onSend() }

    private val PS.stop get() = runCatching { close }.onSuccess {
        if (26.checkDisable && checkSelfPermission()) adapter.disable(); wait
    }

    private fun checkSelfPermission() = if (Build.VERSION.SDK_INT >= 31) { ActivityCompat
        .checkSelfPermission(context, BLUETOOTH_CONNECT) != 0 } else true

    private fun writePress(message: String, source: String) = bD.insertOrUpdateItemPres(
        PressureModel(Calendar.getInstance().time.time.toString(), message, source)
    )

    private fun writeTemp(message: String) = bD.insertOrUpdateItemTemp(
        TempModel(Calendar.getInstance().time.time.toString(), message)
    )

    private val create get() = if (checkSelfPermission())
        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_VAL)) else null

    private val String.check get() = isNotEmpty() && this != "nAn"
    private val Int.checkDisable get() = Build.VERSION.SDK_INT >= this
    private val PS.success get() = trySend(BluetoothResponse.OnSuccess)
    private val PS.wait get() = trySend(BluetoothResponse.Wait)
    private val String.press get() = BluetoothResponse.Press(this)
    private val String.temp get() = BluetoothResponse.Temp(this)
    private val Throwable.error get() = BluetoothResponse.Error(message.toString())
    private val close get() = run { socket?.close() }
    private val connect get() = run { if (checkSelfPermission()) socket?.connect() }
    private val enable get() = run{ if (checkSelfPermission()) adapter.enable() }
    private val delay get() = suspend { run { delay(1000L) } }
    fun getAllTemps() = bD.getAllItemsTemp()
    fun getAllPressure() = bD.getAllItemsPres()
    fun clearPressureList() = bD.deleteAllPres()
    fun clearTempList() = bD.deleteAllTemp()
}

typealias PS = ProducerScope<BluetoothResponse>
typealias BR = BluetoothResponse