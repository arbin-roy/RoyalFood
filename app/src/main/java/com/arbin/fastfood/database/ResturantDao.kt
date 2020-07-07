package com.arbin.fastfood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResturantDao {

    @Insert
    fun addRes(ResturantEntities: ResturantEntities)

    @Delete
    fun delRes(ResturantEntities: ResturantEntities)

    @Query("SELECT * FROM restaurants")
    fun getAllRes(): List<ResturantEntities>

    @Query("SELECT * FROM restaurants WHERE res_id = :resId")
    fun getResById(resId: String): ResturantEntities

    @Query("DELETE FROM restaurants")
    fun delAllRes()
}