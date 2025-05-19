package com.marcel.eatbymark.places.favouriteplaces.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePlacesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouritePlace(place: FavouritePlaceDatabaseModel)

    @Query("SELECT * FROM favourite_places ORDER BY name ASC")
    fun getAllFavouritePlaces(): PagingSource<Int, FavouritePlaceDatabaseModel>

    @Query("SELECT id FROM favourite_places")
    fun getAllFavouritePlacesIds(): Flow<List<String>>

    @Query("DELETE FROM favourite_places WHERE id = :id")
    suspend fun removeFavouritePlace(id: String)
}