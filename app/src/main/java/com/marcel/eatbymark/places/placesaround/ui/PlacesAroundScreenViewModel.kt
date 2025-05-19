package com.marcel.eatbymark.places.placesaround.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcel.eatbymark.places.favouriteplaces.domain.FavouritePlacesUseCaseProvider
import com.marcel.eatbymark.places.favouriteplaces.domain.models.FavouritePlacesUpdateResult
import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.domain.PlacesAroundUseCaseProvider
import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdate
import com.marcel.eatbymark.telemetry.domain.TelemetryUseCaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlacesAroundScreenViewModel @Inject constructor(
    private val favouritePlacesUseCaseProvider: FavouritePlacesUseCaseProvider,
    private val placesAroundUseCaseProvider: PlacesAroundUseCaseProvider,
    private val telemetryUseCaseProvider: TelemetryUseCaseProvider,
) : ViewModel() {
    val viewState: StateFlow<PlacesAroundScreenViewState>
        get() = modelState.map {
            it.toViewState(
                onToggleExpanded = { id, isExpanded, telemetryId ->
                    onIntent(PlacesAroundScreenIntent.ToggleExpanded(id, isExpanded, telemetryId))
                },
                onToggleFavouriteStatus = { id, isFavourite, telemetryId ->
                    onIntent(
                        PlacesAroundScreenIntent.ToggleFavouriteStatus(
                            id,
                            isFavourite,
                            telemetryId
                        )
                    )
                },
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(millisToWaitBeforeCancellingUIRelatedWork),
            initialValue = PlacesAroundScreenViewState()
        )

    private val millisToWaitBeforeCancellingUIRelatedWork = 5000L
    private val modelState = MutableStateFlow(PlacesAroundScreenModel())
    private var placesAroundUpdatesJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        placesAroundUpdatesJob?.cancel()
    }

    fun onIntent(intent: PlacesAroundScreenIntent) {
        when (intent) {
            is PlacesAroundScreenIntent.GetUpdates -> getPlacesAroundUpdates()

            is PlacesAroundScreenIntent.MessageShown -> onMessageShown()

            is PlacesAroundScreenIntent.StopUpdates -> onStopPlacesAroundUpdates()

            is PlacesAroundScreenIntent.ToggleExpanded -> onToggleExpanded(intent)

            is PlacesAroundScreenIntent.ToggleFavouriteStatus -> onToggleFavouritePlaceStatus(intent)
        }
    }

    private fun getPlacesAroundUpdates() {
        modelState.update { state -> state.copy(isStopUpdatesRequested = false) }
        if (placesAroundUpdatesJob != null) return
        placesAroundUpdatesJob = viewModelScope.launch(Dispatchers.IO) {
            placesAroundUseCaseProvider.getPlacesAroundUpdatesUseCase.getUpdates()
                .collect { update ->
                    when (update) {
                        is PlacesAroundUpdate.Error -> {
                            modelState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    messages = onAddMessage(
                                        currentMessages = state.messages,
                                        message = update.error.toPlacesAroundScreenMessage()
                                    ),
                                )
                            }
                        }

                        is PlacesAroundUpdate.Loading -> {
                            modelState.update { state ->
                                state.copy(
                                    isLoading = true,
                                )
                            }
                        }

                        is PlacesAroundUpdate.Success -> {
                            modelState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    places = update.places,
                                    placesIndices = getPlacesIndices(update.places),
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun getPlacesIndices(places: List<Place>): HashMap<String, Int> {
        val indicesMap = HashMap<String, Int>()
        places.forEachIndexed { index, place ->
            indicesMap[place.id] = index
        }
        return indicesMap
    }

    private fun onAddMessage(
        currentMessages: List<PlacesAroundScreenMessage>,
        message: PlacesAroundScreenMessage,
    ): List<PlacesAroundScreenMessage> {
        return currentMessages.toMutableList().run {
            add(message)
            toList()
        }
    }

    private fun onMessageShown() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000) // allows message to be shown for 3 seconds
            modelState.update { state ->
                val updatedMessages = state.messages.toMutableList().run {
                    removeLastOrNull()
                    toList()
                }
                state.copy(messages = updatedMessages)
            }
        }
    }

    private fun onStopPlacesAroundUpdates() {
        modelState.update { state -> state.copy(isStopUpdatesRequested = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(millisToWaitBeforeCancellingUIRelatedWork)
            if (modelState.value.isStopUpdatesRequested) {
                placesAroundUpdatesJob?.cancel()
                placesAroundUpdatesJob = null
            }
        }
    }

    private fun onToggleExpanded(intent: PlacesAroundScreenIntent.ToggleExpanded) {
        viewModelScope.launch(Dispatchers.IO) {
            modelState.update { state ->
                state.copy(
                    expandedItemId = if (intent.isExpanded) intent.id else null,
                )
            }
            if (intent.isExpanded) {
                telemetryUseCaseProvider.recordTelemetryUseCase.recordEvent(
                    intent.toTelemetryEvent()
                )
            }
        }
    }

    private fun onToggleFavouritePlaceStatus(intent: PlacesAroundScreenIntent.ToggleFavouriteStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaceFavouriteStatus(intent.id, intent.isFavourite)
            val result = if (intent.isFavourite) {
                val state = modelState.value
                state.placesIndices[intent.id]?.let { index ->
                    favouritePlacesUseCaseProvider.addFavouritePlaceUseCase.addFavouritePlace(
                        state.places[index]
                    )
                }
            } else {
                favouritePlacesUseCaseProvider.removeFavouritePlaceUseCase.removeFavouritePlace(
                    intent.id
                )
            }
            if (result is FavouritePlacesUpdateResult.Error) {
                updatePlaceFavouriteStatus(intent.id, !intent.isFavourite)
                modelState.update { state ->
                    val messages = onAddMessage(
                        currentMessages = state.messages,
                        PlacesAroundScreenMessage.FavouritePlaceToggleError,
                    )
                    state.copy(
                        messages = messages
                    )
                }
            } else {
                telemetryUseCaseProvider.recordTelemetryUseCase.recordEvent(
                    intent.toTelemetryEvent()
                )
            }
        }
    }

    private fun updatePlaceFavouriteStatus(id: String, isFavourite: Boolean) {
        modelState.update { state ->
            val index = state.placesIndices[id]
            if (index != null) { // List could have been changed
                val updatedPlaces = state.places.toMutableList().run {
                    set(index, state.places[index].copy(isFavourite = isFavourite))
                    toList()
                }
                state.copy(
                    places = updatedPlaces
                )
            } else {
                state
            }
        }
    }
}

