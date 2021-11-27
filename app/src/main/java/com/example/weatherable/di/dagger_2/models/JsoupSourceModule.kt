package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.data.internet.jsoup.JsoupSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class JsoupSourceModule {
    @Provides
    @Singleton
    fun  jsoupDatasource(): JsoupSource {
        return JsoupSource()
    }
}