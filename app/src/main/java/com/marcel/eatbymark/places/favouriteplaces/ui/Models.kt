package com.marcel.eatbymark.places.favouriteplaces.ui

import androidx.paging.PagingData
import androidx.paging.map
import com.marcel.eatbymark.components.models.PlaceListItemViewState
import com.marcel.eatbymark.components.models.toPlaceListItemViewState
import com.marcel.eatbymark.places.models.Place

sealed interface FavouritePlacesScreenIntent {
    data object PageLoading : FavouritePlacesScreenIntent
    data object PageLoaded : FavouritePlacesScreenIntent
    data object PageError : FavouritePlacesScreenIntent
    data class RemoveFavourite(
        val id: String,
    ) : FavouritePlacesScreenIntent
}

enum class FavouritePlacesScreenMessage {
    ApplicationError, FavouritePlaceToggleError, UnknownError
}

data class FavouritePlacesScreenModel(
    val isLoading: Boolean = true,
    val messages: List<FavouritePlacesScreenMessage> = emptyList(),
)

data class FavouritePlacesScreenViewState(
    val isLoading: Boolean = false,
    val message: FavouritePlacesScreenMessage? = null,
    val places: PagingData<PlaceListItemViewState> = PagingData.empty(),
)

fun FavouritePlacesScreenModel.toViewState(
    places: PagingData<Place>,
    onToggleFavouriteStatus: (id: String, isFavourite: Boolean, telemetryId: String?) -> Unit,
) = FavouritePlacesScreenViewState(
    isLoading = isLoading,
    message = messages.firstOrNull(),
    places = places.map { place ->
        place.toPlaceListItemViewState(
            isExpanded = false,
            onToggleExpanded = { _, _, _ -> },
            onToggleFavouriteStatus = onToggleFavouriteStatus,
        )
    },
)