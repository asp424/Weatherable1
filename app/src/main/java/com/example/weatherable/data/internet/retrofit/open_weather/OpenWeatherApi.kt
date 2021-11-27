package com.example.weatherable.data.internet.retrofit.open_weather

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

//c4acc7d9b8b7752fbeb0a80870348db0
interface OpenWeatherApi {
    @GET("/forecast")
    fun getOpenWeather(): Flow<WeatherResponse>



}

data class WeatherResponse(
    val lat: String,
    val lon: String,
    val cnt:String,
    val appId: String

)





