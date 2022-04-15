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
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class BluetoothSource @Inject constructor(
    private val context: Application,
    private val bluetoothDataDao: BluetoothDataDao
) {

    private var socket: BluetoothSocket? = null

    private val adapter by lazy {
        (context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE)
                as BluetoothManager).adapter
    }

    private val device by lazy { adapter.getRemoteDevice(DEVICE) }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun runBluetooth(source: String) = callbackFlow {
        launch(Default) {
             task({ if (checkSelfPermission()) adapter.enable() },
                    { task({ socket?.close() },
                        { task({ delay(1000L); socket = create },
                            { task({ work(source) { trySend(it) } }, {}, 1) }) }) })  }
        awaitClose { stop }
    }.flowOn(IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    private inline fun ProducerScope<BluetoothResponse>.task(
        run: () -> Unit, suc: () -> Unit, type: Int = 0
    ) = runCatching { run() }.onSuccess { suc() }.onFailure { if (type == 1) stop; trySend(it.error) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private inline fun ProducerScope<BluetoothResponse>.work(
        source: String,
        onSend: (BluetoothResponse) -> Unit
    ) {
        var counter = 0; var writeTemp = true; var writePres = true
        if (checkSelfPermission()) {
            socket?.connect(); success
            socket?.inputStream?.bufferedReader().use { reader ->
                while (isActive) {
                    reader?.readLine()?.apply {
                        if (isNotEmpty() && this != "nAn") {
                            when (counter) {
                                0 -> {
                                    if (writeTemp) {
                                        writeTemp(this); writeTemp = false
                                    }
                                    onSend(this.temp); counter = 1
                                }
                                1 -> {
                                    if (writePres) {
                                        writePress(this, source); writePres = false
                                    }
                                    onSend(this.press); counter = 0
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ProducerScope<BluetoothResponse>.stop
        get() = runCatching { socket?.close() }.onSuccess {
            if (checkDisable && checkSelfPermission()) adapter.disable(); wait
        }

    private fun checkSelfPermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ActivityCompat.checkSelfPermission(context, BLUETOOTH_CONNECT) != 0
    } else true

    private val checkDisable get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    private fun writePress(message: String, source: String) =
        bluetoothDataDao.insertOrUpdateItemPres(
            PressureModel(Calendar.getInstance().time.time.toString(), message, source)
        )

    private fun writeTemp(message: String) = bluetoothDataDao.insertOrUpdateItemTemp(
        TempModel(Calendar.getInstance().time.time.toString(), message)
    )

    private val String.press get() = BluetoothResponse.Press(this)
    private val String.temp get() = BluetoothResponse.Temp(this)
    private val Throwable.error get() = BluetoothResponse.Error(message.toString())

    private val create
        get() = if (checkSelfPermission())
            device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_VAL)) else null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ProducerScope<BluetoothResponse>.success get() = trySend(BluetoothResponse.OnSuccess)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ProducerScope<BluetoothResponse>.wait
        get() = trySend(BluetoothResponse.Wait)

    fun getAllTemps(): List<TempModel>? = bluetoothDataDao.getAllItemsTemp()

    fun getAllPressure(): List<PressureModel> = bluetoothDataDao.getAllItemsPres()

    fun clearPressureList() = bluetoothDataDao.deleteAllPres()

    fun clearTempList() = bluetoothDataDao.deleteAllTemp()
}

