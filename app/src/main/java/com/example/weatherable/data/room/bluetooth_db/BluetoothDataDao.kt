package com.example.weatherable.data.room.bluetooth_db

import androidx.room.*
import com.example.weatherable.data.room.bluetooth_db.models.PressureModel
import com.example.weatherable.data.room.bluetooth_db.models.TempModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDataDao {
    @Query("delete from temp_table where id = :id")
    fun deleteByIdTemp(id: Int)
    @Query("delete from pressure_table where id = :id")
    fun deleteByIdPres(id: Int)
    @Delete
    fun deleteTemp(item: TempModel)
    @Delete
    fun deletePres(item: PressureModel)
    @Query("select * from temp_table")
    fun getAllItemsTemp(): List<TempModel>?
    @Query("select * from pressure_table")
    fun getAllItemsPres(): List<PressureModel>?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateItemTemp(item: TempModel): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateItemPres(item: PressureModel): Long
    @Query("DELETE FROM temp_table")
    fun deleteAllTemp()
    @Query("DELETE FROM pressure_table")
    fun deleteAllPres()
    @Query("DELETE FROM temp_table WHERE id = :id")
    fun deleteItemTemp(id: String)
    @Query("DELETE FROM pressure_table WHERE id = :id")
    fun deleteItemPres(id: String)
}
