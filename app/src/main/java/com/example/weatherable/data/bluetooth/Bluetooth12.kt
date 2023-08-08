package com.example.weatherable.data.bluetooth

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothManager
import android.content.Intent
import com.example.weatherable.data.room.bluetooth_db.BluetoothDataDao
import com.example.weatherable.utilites.DEVICE
import javax.inject.Inject

class Bluetooth12 @Inject constructor(
    private val context: Application,
    private val bD: BluetoothDataDao
) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager?.adapter


    private fun connect() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(ACTION_REQUEST_ENABLE)
            val device by lazy { bluetoothAdapter.getRemoteDevice(DEVICE) }
        }
    }
}


