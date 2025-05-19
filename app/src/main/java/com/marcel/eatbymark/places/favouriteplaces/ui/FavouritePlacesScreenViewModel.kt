package com.marcel.eatbymark.places.favouriteplaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marcel.eatbymark.places.favouriteplaces.domain.FavouritePlacesUseCaseProvider
import com.marcel.eatbymark.places.favouriteplaces.domain.models.FavouritePlacesUpdateResult
import com.marcel.eatbymark.places.models.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritePlacesScreenViewModel @Inject constructor(
    private val favouritePlacesUseCaseProvider: FavouritePlacesUseCaseProvider
) : ViewModel() {
    private val favouritePlacesPagingDataFlow: Flow<PagingData<Place>> =
        favouritePlacesUseCaseProvider.getAllFavouritePlacesUseCase.getAllFavouritePlaces()
            .cachedIn(viewModelScope)

    val viewState: StateFlow<FavouritePlacesScreenViewState>
        get() = combine(
            favouritePlacesPagingDataFlow,
            modelState
        ) { placesPagingData, model ->
            model.toViewState(
                places = placesPagingData
            ) { id, isFavourite, telemetryId ->
                onIntent(FavouritePlacesScreenIntent.RemoveFavourite(id = id))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavouritePlacesScreenViewState() // Ensure initial value matches the new structure
        )

    private val modelState = MutableStateFlow(FavouritePlacesScreenModel())
    private var favouritesJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        favouritesJob?.cancel()
    }

    fun onIntent(intent: FavouritePlacesScreenIntent) {
        when (intent) {
            FavouritePlacesScreenIntent.PageError -> {
                modelState.update { state ->
                    state.copy(
                        isLoading = false,
                        messages = onAddMessage(
                            currentMessages = state.messages,
                            FavouritePlacesScreenMessage.ApplicationError,
                        )
                    )
                }
            }

            FavouritePlacesScreenIntent.PageLoading -> {
                modelState.update { state -> state.copy(isLoading = true) }
            }

            FavouritePlacesScreenIntent.PageLoaded -> {
                modelState.update { state -> state.copy(isLoading = false) }
            }

            is FavouritePlacesScreenIntent.RemoveFavourite -> onRemoveFavourite(intent.id)
        }
    }

    private fun onAddMessage(
        currentMessages: List<FavouritePlacesScreenMessage>,
        message: FavouritePlacesScreenMessage,
    ): List<FavouritePlacesScreenMessage> {
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

    private fun onRemoveFavourite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                favouritePlacesUseCaseProvider.removeFavouritePlaceUseCase.removeFavouritePlace(
                    id
                )
            if (result is FavouritePlacesUpdateResult.Error) {
                modelState.update { state ->
                    val messages = onAddMessage(
                        currentMessages = state.messages,
                        FavouritePlacesScreenMessage.FavouritePlaceToggleError,
                    )
                    state.copy(
                        messages = messages
                    )
                }
            }
        }
    }
}