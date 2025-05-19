package com.marcel.eatbymark.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlaceDatabaseModel
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesDAO

data class DatabaseConfig(
    val databaseName: String = "eat-by-mark-database"
)

@Database(entities = [FavouritePlaceDatabaseModel::class], version = 1)
abstract class EATByMarkDatabase : RoomDatabase() {
    abstract fun favouritePlacesDAO(): FavouritePlacesDAO
}