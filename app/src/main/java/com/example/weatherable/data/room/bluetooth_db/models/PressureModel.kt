package com.example.weatherable.data.room.bluetooth_db.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pressure_table")
data class PressureModel(
    @NonNull
    @PrimaryKey
    var id: String = "", var pressure: String = "", var type: String = "")