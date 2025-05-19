package com.marcel.eatbymark.places.favouriteplaces.domain

import com.marcel.eatbymark.core.BackgroundDispatcher
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class FavouritePlacesUseCaseProvider @Inject constructor(
    @BackgroundDispatcher backgroundDispatcher: CoroutineDispatcher,
    favouritePlacesRepository: FavouritePlacesRepository
) {
    val addFavouritePlaceUseCase: AddFavouritePlaceUseCase =
        AddFavouritePlaceUseCase(backgroundDispatcher, favouritePlacesRepository)
    val removeFavouritePlaceUseCase: RemoveFavouritePlaceUseCase =
        RemoveFavouritePlaceUseCase(backgroundDispatcher, favouritePlacesRepository)
    val getAllFavouritePlacesUseCase: GetAllFavouritePlacesUseCase =
        GetAllFavouritePlacesUseCase(backgroundDispatcher, favouritePlacesRepository)
}