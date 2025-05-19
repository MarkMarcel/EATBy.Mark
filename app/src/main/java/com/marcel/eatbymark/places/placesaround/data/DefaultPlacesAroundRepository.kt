package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.core.ApplicationScope
import com.marcel.eatbymark.places.currentlocation.data.CurrentLocationDataSource
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesDataSource
import com.marcel.eatbymark.places.favouriteplaces.domain.models.GetFavouritePlacesIdsResult
import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig
import com.marcel.eatbymark.places.placesaround.data.models.GetPlacesAroundError
import com.marcel.eatbymark.places.placesaround.data.models.toPlacesUpdateError
import com.marcel.eatbymark.places.placesaround.domain.PlacesAroundRepository
import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdate
import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdateError
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class DefaultPlacesAroundRepository @Inject constructor(
    @ApplicationScope applicationScope: CoroutineScope,
    currentLocationDataSource: CurrentLocationDataSource,
    favouritePlacesDataSource: FavouritePlacesDataSource,
    placesAroundConfig: PlacesAroundConfig,
    private val placesAroundDataSource: PlacesAroundDataSource,
) : PlacesAroundRepository {
    /* Since the application is simulating the location provide,
    immediately start collecting location
     */
    private val favouritePlacesIds = favouritePlacesDataSource
        .getAllFavouritePlacesIds()
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )
    private val latestKnownLocation = currentLocationDataSource
        .getCurrentLocation(
            updatesIntervalSeconds = placesAroundConfig.updatesIntervalSeconds
        )
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPlacesAroundUpdates(): Flow<PlacesAroundUpdate> {
        /* Using flatMapLatest to ensure 2 things
        *  1. Cancel fetch of places if a new location is available
        *  2. Output of loading and error state
        * */
        return latestKnownLocation.filterNotNull()
            .flatMapLatest { coordinate ->
                flow {
                    emit(PlacesAroundUpdate.Loading)
                    placesAroundDataSource.getPlacesAround(
                        latitude = coordinate.latitude,
                        longitude = coordinate.longitude
                    ).onSuccess { places ->
                        emit(PlacesAroundUpdate.Success(places))
                    }.onFailure { exception ->
                        if (exception is GetPlacesAroundError.NetworkError) {
                            throw exception
                        } else {
                            handleException(exception)
                        }
                    }
                }.retryWhen { cause, attempt ->
                    if (attempt < 3 && cause is GetPlacesAroundError.NetworkError) {
                        delay(1000 * (attempt + 1)) // Increase period between retries
                        true
                    } else {
                        false
                    }
                }.catch { exception ->
                    handleException(exception)
                }
            }
            .combine(favouritePlacesIds) { placesAroundUpdate, favouritePlaceIdsResult ->
                if (
                    (placesAroundUpdate is PlacesAroundUpdate.Success)
                    && (favouritePlaceIdsResult is GetFavouritePlacesIdsResult.Success)
                ) {
                    val placesWithFavouriteStatus =
                        setPlacesFavouriteStatus(
                            favouritePlaceIdsResult.favouritePlacesIds,
                            placesAroundUpdate.places
                        )
                    PlacesAroundUpdate.Success(placesWithFavouriteStatus)
                } else {
                    placesAroundUpdate
                }
            }
            .catch { e -> // Catch errors from upstream (latestKnownLocation or flatMapLatest)
                if (e is CancellationException) throw e
                handleException(e)
            }
    }

    private fun setPlacesFavouriteStatus(
        favouritePlacesIds: List<String>,
        places: List<Place>
    ): List<Place> {
        return places.map { place ->
            place.copy(isFavourite = favouritePlacesIds.contains(place.id))
        }
    }

    private suspend fun FlowCollector<PlacesAroundUpdate>.handleException(exception: Throwable) {
        if (exception is GetPlacesAroundError) {
            emit(PlacesAroundUpdate.Error(exception.toPlacesUpdateError()))
        } else {
            emit(PlacesAroundUpdate.Error(PlacesAroundUpdateError.Unknown))
        }
    }
}