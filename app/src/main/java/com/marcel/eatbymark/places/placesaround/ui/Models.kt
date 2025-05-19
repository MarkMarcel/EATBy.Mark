package com.marcel.eatbymark.places.placesaround.ui

import com.marcel.eatbymark.components.models.PlaceListItemViewState
import com.marcel.eatbymark.components.models.toPlaceListItemViewState
import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdateError
import com.marcel.eatbymark.telemetry.domain.ATTR_PLACE_ID
import com.marcel.eatbymark.telemetry.domain.ATTR_TELEMETRY_ID
import com.marcel.eatbymark.telemetry.domain.TelemetryEvent
import com.marcel.eatbymark.telemetry.domain.TelemetryEventType

sealed interface PlacesAroundScreenIntent {
    data object GetUpdates : PlacesAroundScreenIntent
    data object MessageShown : PlacesAroundScreenIntent
    data object StopUpdates : PlacesAroundScreenIntent
    data class ToggleExpanded(
        val id: String,
        val isExpanded: Boolean,
        val telemetryId: String?,
    ) : PlacesAroundScreenIntent

    data class ToggleFavouriteStatus(
        val id: String,
        val isFavourite: Boolean,
        val telemetryId: String?,
    ) : PlacesAroundScreenIntent
}

enum class PlacesAroundScreenMessage {
    ApplicationError, FavouritePlaceToggleError, NetworkError, UnknownError
}

data class PlacesAroundScreenModel(
    val expandedItemId: String? = null,
    val isLoading: Boolean = true, // Default to true to show loading on initial screen view
    val isStopUpdatesRequested: Boolean = false,
    val messages: List<PlacesAroundScreenMessage> = emptyList(),
    val places: List<Place> = emptyList(),
    val placesIndices: HashMap<String, Int> = hashMapOf(),
)

data class PlacesAroundScreenViewState(
    val expandedItemIndex: Int? = null,
    val isLoading: Boolean = false,
    val message: PlacesAroundScreenMessage? = null,
    val places: List<PlaceListItemViewState> = emptyList(),
)

fun PlacesAroundScreenModel.toViewState(
    onToggleExpanded: (id: String, isExpanded: Boolean, telemetryId: String?) -> Unit,
    onToggleFavouriteStatus: (id: String, isFavourite: Boolean, telemetryId: String?) -> Unit,
) = PlacesAroundScreenViewState(
    expandedItemIndex = placesIndices[expandedItemId],
    isLoading = isLoading,
    message = messages.firstOrNull(),
    places = places.map { place ->
        place.toPlaceListItemViewState(
            isExpanded = expandedItemId == place.id,
            onToggleExpanded = onToggleExpanded,
            onToggleFavouriteStatus = onToggleFavouriteStatus,
        )
    },
)

fun PlacesAroundUpdateError.toPlacesAroundScreenMessage() = when (this) {
    PlacesAroundUpdateError.Application -> PlacesAroundScreenMessage.ApplicationError
    PlacesAroundUpdateError.Network -> PlacesAroundScreenMessage.NetworkError
    PlacesAroundUpdateError.Server -> PlacesAroundScreenMessage.UnknownError
    PlacesAroundUpdateError.Unknown -> PlacesAroundScreenMessage.UnknownError
}

fun PlacesAroundScreenIntent.ToggleFavouriteStatus.toTelemetryEvent() = TelemetryEvent(
    eventType = TelemetryEventType.Favourited,
    attributes = mapOf(
        ATTR_PLACE_ID to id,
        ATTR_TELEMETRY_ID to telemetryId,
    )
)

fun PlacesAroundScreenIntent.ToggleExpanded.toTelemetryEvent() = TelemetryEvent(
    eventType = TelemetryEventType.Expanded,
    attributes = mapOf(
        ATTR_PLACE_ID to id,
        ATTR_TELEMETRY_ID to telemetryId,
    )
)
