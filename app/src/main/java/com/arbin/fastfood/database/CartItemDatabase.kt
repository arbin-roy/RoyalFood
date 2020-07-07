package com.arbin.fastfood.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartItemEntities::class], version = 1)
abstract class CartItemDatabase: RoomDatabase() {

    abstract fun CartItemDao(): CartItemDao

}