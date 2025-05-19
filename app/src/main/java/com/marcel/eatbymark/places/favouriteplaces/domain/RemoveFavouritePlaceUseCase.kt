package com.marcel.eatbymark.places.favouriteplaces.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoveFavouritePlaceUseCase(
    private val backgroundDispatcher: CoroutineDispatcher,
    private val favouritePlacesRepository: FavouritePlacesRepository
) {
    suspend fun removeFavouritePlace(placeId: String) = withContext(backgroundDispatcher) {
        favouritePlacesRepository.removeFavouritePlace(placeId)
    }
}