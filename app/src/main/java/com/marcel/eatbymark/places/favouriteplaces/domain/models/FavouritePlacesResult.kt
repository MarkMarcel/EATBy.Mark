package com.marcel.eatbymark.places.favouriteplaces.domain.models

sealed interface FavouritePlacesUpdateResult {
    data object Error : FavouritePlacesUpdateResult
    data object Success : FavouritePlacesUpdateResult
}

sealed interface GetFavouritePlacesIdsResult {
    data object Error : GetFavouritePlacesIdsResult
    data class Success(val favouritePlacesIds: List<String>) : GetFavouritePlacesIdsResult
}