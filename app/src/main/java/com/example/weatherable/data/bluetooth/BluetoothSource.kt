package com.example.weatherable.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Build
import com.example.weatherable.data.room.bluetooth_db.BluetoothDataDao
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import com.example.weatherable.data.view_states.BluetoothResponse
import com.example.weatherable.utilites.DEVICE
import com.example.weatherable.utilites.UUID_VAL
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import javax.inject.Inject

class BluetoothSource @Inject constructor(private val bluetoothDataDao: BluetoothDataDao) {
    private var bluetoothSocket: BluetoothSocket? = null
    private val btAdapter = BluetoothAdapter.getDefaultAdapter()
    private val device = btAdapter.getRemoteDevice(DEVICE)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun connect(): Flow<BluetoothResponse> = callbackFlow {
        launch(Dispatchers.Default) {
            runCatching {
                btAdapter?.enable()
            }.onSuccess {
                if (it!!) runCatching { bluetoothSocket?.close() }
                    .onSuccess {
                        runCatching {
                            delay(1000L)
                            bluetoothSocket = device.createRfcommSocketToServiceRecord(
                                UUID.fromString(UUID_VAL)
                            )
                        }.onSuccess {
                            runCatching {
                                var counter = 0
                                var writeTemp = true
                                var writePres = true
                                bluetoothSocket!!.connect()
                                if (bluetoothSocket != null) {
                                    trySend(BluetoothResponse.OnSuccess)
                                    bluetoothSocket!!.inputStream.bufferedReader().use { reader ->
                                        while (isActive) {
                                            val message = reader.readLine()
                                            if (message.isNotEmpty() && message != "nAn") {
                                                when (counter) {
                                                    0 -> {
                                                        if (writeTemp)
                                                            CoroutineScope(Dispatchers.Default).launch {
                                                                bluetoothDataDao.insertOrUpdateItemTemp(
                                                                    TempModel(
                                                                        id = Calendar.getInstance().time.time.toString(),
                                                                        temp = message.toString()
                                                                    )
                                                                )
                                                                writeTemp = false
                                                            }
                                                        trySend(BluetoothResponse.Temp(message.toString()))
                                                        counter = 1
                                                    }
                                                    1 -> {
                                                        if (writePres)
                                                        CoroutineScope(Dispatchers.Default).launch {
                                                            bluetoothDataDao.insertOrUpdateItemPres(
                                                                PressureModel(
                                                                    id = Calendar.getInstance().time.time.toString(),
                                                                    pressure = message.toString()
                                                                )
                                                            )
                                                            writePres = false
                                                        }
                                                        trySend(
                                                            BluetoothResponse.Press(
                                                                message.toString()
                                                            )
                                                        )
                                                        counter = 0
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }.onFailure {
                                runCatching {
                                    bluetoothSocket?.close()
                                }.onFailure { e ->
                                    trySend(BluetoothResponse.Error(e.message.toString()))
                                }.onSuccess { //btAdapter?.disable()
                                }
                            }
                                .onFailure { e -> trySend(BluetoothResponse.Error(e.message.toString())) }
                        }.onFailure { e -> trySend(BluetoothResponse.Error(e.message.toString())) }
                    }
                    .onFailure { e -> trySend(BluetoothResponse.Error(e.message.toString())) }
            }
        }
        awaitClose {
            runCatching { bluetoothSocket?.close() }.onSuccess {
                //btAdapter?.disable()
                trySendBlocking(BluetoothResponse.Wait)
            }.onFailure { e -> trySendBlocking(BluetoothResponse.Error(e.message.toString())) }
        }
    }
   fun getAllTemps(): List<TempModel>? = bluetoothDataDao.getAllItemsTemp()
   fun getAllPressure(): List<PressureModel> = bluetoothDataDao.getAllItemsPres()
   fun clearPressureList() = bluetoothDataDao.deleteAllPres()
   fun clearTempList() = bluetoothDataDao.deleteAllTemp()
}