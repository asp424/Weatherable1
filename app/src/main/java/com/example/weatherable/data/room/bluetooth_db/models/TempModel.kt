package com.example.weatherable.data.room.bluetooth_db.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "temp_table")
class TempModel(
    @NonNull
    @PrimaryKey
    var id: String = "",
    var temp: String? = "",
    )