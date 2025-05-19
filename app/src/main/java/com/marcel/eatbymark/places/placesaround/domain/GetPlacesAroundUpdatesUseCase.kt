package com.marcel.eatbymark.places.placesaround.domain

import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetPlacesAroundUpdatesUseCase(
    private val repository: PlacesAroundRepository,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    fun getUpdates(): Flow<PlacesAroundUpdate> =
        repository.getPlacesAroundUpdates()
            .map { update ->
                when (update) {
                    is PlacesAroundUpdate.Error -> update
                    is PlacesAroundUpdate.Loading -> update
                    is PlacesAroundUpdate.Success -> {
                        update.copy(
                            places = update.places.sortedBy { it.name }
                        )
                    }
                }
            }
            .flowOn(backgroundDispatcher)
}