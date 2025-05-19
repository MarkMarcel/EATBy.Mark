package com.marcel.eatbymark.places.favouriteplaces.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn

class GetAllFavouritePlacesUseCase(
    private val backgroundDispatcher: CoroutineDispatcher,
    private val favouritePlacesRepository: FavouritePlacesRepository
) {
    fun getAllFavouritePlaces() =
        favouritePlacesRepository
            .getAllFavouritePlaces()
            .flowOn(backgroundDispatcher)
}