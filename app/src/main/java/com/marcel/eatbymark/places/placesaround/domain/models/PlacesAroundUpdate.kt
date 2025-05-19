package com.marcel.eatbymark.places.placesaround.domain.models

import com.marcel.eatbymark.places.models.Place

sealed class PlacesAroundUpdate {
    data class Error(val error: PlacesAroundUpdateError) : PlacesAroundUpdate()
    data object Loading : PlacesAroundUpdate()
    data class Success(val places: List<Place>) : PlacesAroundUpdate()
}

enum class PlacesAroundUpdateError {
    Application, Network, Server, Unknown
}