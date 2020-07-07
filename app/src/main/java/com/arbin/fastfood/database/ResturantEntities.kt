package com.arbin.fastfood.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class ResturantEntities(
    @PrimaryKey val res_id: String,
    @ColumnInfo(name = "res_name") val res_name: String,
    @ColumnInfo(name = "res_rating") val res_rating: String,
    @ColumnInfo(name = "cost_for_one") val cost_for_one: String,
    @ColumnInfo(name = "res_image") val res_image: String
)
