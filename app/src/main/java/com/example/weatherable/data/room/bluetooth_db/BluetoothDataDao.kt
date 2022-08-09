package com.example.weatherable.data.room.bluetooth_db

import androidx.room.*
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDataDao {
    @Query(getAllTemp)
    fun getAllItemsTemp(): Flow<List<TempModel>>

    @Query(getAllPress)
    fun getAllItemsPres(): Flow<List<PressureModel>>

    @Insert(onConflict = replace)
    fun insertOrUpdateItemTemp(item: TempModel): Long

    @Insert(onConflict = replace)
    fun insertOrUpdateItemPres(item: PressureModel): Long

    @Query(delAllTemp)
    fun deleteAllTemp()

    @Query(delAllPress)
    fun deleteAllPres()
}

private const val replace = OnConflictStrategy.REPLACE
private const val getAllTemp = "select * from temp_table"
private const val getAllPress = "select * from pressure_table"
private const val delAllPress = "DELETE FROM pressure_table"
private const val delAllTemp = "DELETE FROM temp_table"

