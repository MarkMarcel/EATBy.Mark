package com.marcel.eatbymark.places.placesaround.domain

import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdate
import kotlinx.coroutines.flow.Flow

interface PlacesAroundRepository {
    fun getPlacesAroundUpdates(): Flow<PlacesAroundUpdate>
}