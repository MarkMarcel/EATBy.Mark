package com.marcel.eatbymark.places.placesaround.domain

import com.marcel.eatbymark.core.BackgroundDispatcher
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class PlacesAroundUseCaseProvider @Inject constructor(
    @BackgroundDispatcher private val backgroundDispatcher: CoroutineDispatcher,
    private val placesAroundRepository: PlacesAroundRepository,
) {
    val getPlacesAroundUpdatesUseCase: GetPlacesAroundUpdatesUseCase
        get() = GetPlacesAroundUpdatesUseCase(
            backgroundDispatcher = backgroundDispatcher,
            repository = placesAroundRepository
        )
}