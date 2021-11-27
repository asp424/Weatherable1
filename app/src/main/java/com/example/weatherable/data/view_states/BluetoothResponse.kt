package com.example.weatherable.data.view_states

import org.json.JSONObject

sealed class BluetoothResponse {
    data class Temp(
        val temp: String
    ): BluetoothResponse()
    data class Press(
        val press: String
    ): BluetoothResponse()
    object Loading: BluetoothResponse()
    data class Error(val messageError: String): BluetoothResponse()
    object Wait: BluetoothResponse()
    object OnSuccess: BluetoothResponse()
    object Start: BluetoothResponse()
    val result: Any
        get() = when(this){
            is Temp -> temp
            is Press -> press
            is Error -> messageError
            else -> {}
        }
}