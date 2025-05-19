package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.places.models.Place

interface PlacesAroundDataSource {
    suspend fun getPlacesAround(
        latitude: Double,
        longitude: Double,
    ): Result<List<Place>>
}