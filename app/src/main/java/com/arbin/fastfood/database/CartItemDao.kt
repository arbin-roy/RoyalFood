package com.arbin.fastfood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartItemDao {

    @Insert
    fun addItem(CartItemEntities: CartItemEntities)

    @Delete
    fun delItem(CartItemEntities: CartItemEntities)

    @Query("SELECT * FROM CartItem")
    fun getAllItem(): List<CartItemEntities>

    @Query("SELECT * FROM CartItem WHERE food_item_id= :foodId")
    fun getItembyId(foodId: String): CartItemEntities

    @Query("DELETE FROM CartItem")
    fun delAllItem()

}