package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.data.internet.jsoup.JsoupSource
import com.example.weatherable.data.internet.retrofit.RestSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RestSourceModule {
    @Provides
    @Singleton
    fun  restSource(): RestSource {
        return RestSource()
    }
}