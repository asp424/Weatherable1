package com.example.weatherable.di.dagger_2.models

import androidx.room.Room
import com.example.weatherable.application.App
import com.example.weatherable.data.bluetooth.BluetoothSource
import com.example.weatherable.data.room.bluetooth_db.BluetoothDataDao
import com.example.weatherable.data.room.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providesBluetoothDatabase(app: App): Database {
        return Room.databaseBuilder(app, Database::class.java, "ass").build()
    }
}
@Module
class BluetoothDaoModule {
    @Provides
    @Singleton
    fun providesBluetoothDataDao(db: Database): BluetoothDataDao {
        return db.bluetoothDao()
    }
}
@Module
class BluetoothSourceModule{
    @Provides
    @Singleton
    fun providesBluetoothSource(
        bluetoothDataDao: BluetoothDataDao
    ): BluetoothSource {
        return BluetoothSource(bluetoothDataDao)
    }
}



