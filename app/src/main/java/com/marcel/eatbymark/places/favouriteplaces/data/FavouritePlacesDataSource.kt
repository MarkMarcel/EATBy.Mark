package com.marcel.eatbymark.places.favouriteplaces.data

import androidx.paging.PagingData
import com.marcel.eatbymark.places.favouriteplaces.domain.models.FavouritePlacesUpdateResult
import com.marcel.eatbymark.places.favouriteplaces.domain.models.GetFavouritePlacesIdsResult
import com.marcel.eatbymark.places.models.Place
import kotlinx.coroutines.flow.Flow

interface FavouritePlacesDataSource {
    suspend fun addFavouritePlace(place: Place): FavouritePlacesUpdateResult
    suspend fun removeFavouritePlace(placeId: String): FavouritePlacesUpdateResult
    fun getAllFavouritePlaces(): Flow<PagingData<Place>>
    fun getAllFavouritePlacesIds(): Flow<GetFavouritePlacesIdsResult>
}