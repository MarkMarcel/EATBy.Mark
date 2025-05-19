package com.marcel.eatbymark.places.favouriteplaces.domain

import com.marcel.eatbymark.places.models.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddFavouritePlaceUseCase(
    private val backgroundDispatcher: CoroutineDispatcher,
    private val favouritePlacesRepository: FavouritePlacesRepository
) {
    suspend fun addFavouritePlace(place: Place) = withContext(backgroundDispatcher) {
        favouritePlacesRepository.addFavouritePlace(place)
    }
}