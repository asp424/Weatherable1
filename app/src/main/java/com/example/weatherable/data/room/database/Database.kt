package com.example.weatherable.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherable.data.room.bluetooth_db.BluetoothDataDao
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel

@Database(entities = [TempModel::class, PressureModel::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun bluetoothDao(): BluetoothDataDao

}