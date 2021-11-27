package com.example.weatherable.data.view_states

import org.json.JSONObject

sealed class InternetResponse {
    data class OnSuccess(
        val dataValues: JSONObject): InternetResponse()
    object Loading: InternetResponse()
    val result: Any?
    get() = when(this){
        is OnSuccess -> dataValues
        else -> {}
    }
}