package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.data.bluetooth.BluetoothSource
import com.example.weatherable.data.internet.jsoup.JsoupSource
import com.example.weatherable.data.internet.retrofit.RestSource
import com.example.weatherable.data.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
    class RepositoryModule {
        @Provides
        @Singleton
        fun repository(
            bluetoothDataSource: BluetoothSource,
            jsoupDatasource: JsoupSource,
            restSource: RestSource
            ): Repository {
            return Repository(bluetoothDataSource, jsoupDatasource, restSource)
        }
    }

