package com.arbin.fastfood.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ResturantEntities::class], version = 1)
abstract class ResturantDatabase: RoomDatabase() {

    abstract fun ResturantDao(): ResturantDao

}
