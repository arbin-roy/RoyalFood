package com.arbin.fastfood.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "CartItem")
data class CartItemEntities(
    @PrimaryKey val food_item_id: String,
    @ColumnInfo(name = "food-name") val food_name: String,
    @ColumnInfo(name = "food-price") val food_price: String,
    @ColumnInfo(name = "res-id") val restaurant_id: String
)