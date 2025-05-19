package com.marcel.eatbymark.places.favouriteplaces.data

import androidx.paging.PagingData
import com.marcel.eatbymark.core.ApplicationScope
import com.marcel.eatbymark.places.favouriteplaces.domain.FavouritePlacesRepository
import com.marcel.eatbymark.places.favouriteplaces.domain.models.FavouritePlacesUpdateResult
import com.marcel.eatbymark.places.models.Place
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class DefaultFavouritePlacesRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val favouritePlacesDataSource: FavouritePlacesDataSource,
) : FavouritePlacesRepository {
    override suspend fun addFavouritePlace(place: Place): FavouritePlacesUpdateResult {
        /* Using application scope to ensure favourites is updated even if calling
        * coroutine scope is cancelled */
        val job = applicationScope.async {
            favouritePlacesDataSource.addFavouritePlace(place)
        }
        return try {
            job.await()
        } catch (_: Exception) {
            FavouritePlacesUpdateResult.Error
        }
    }

    override suspend fun removeFavouritePlace(placeId: String): FavouritePlacesUpdateResult {
        /* Using application scope to ensure favourites is updated even if calling
        * coroutine scope is cancelled */
        val job = applicationScope.async {
            favouritePlacesDataSource.removeFavouritePlace(placeId)
        }
        return try {
            job.await()
        } catch (_: Exception) {
            FavouritePlacesUpdateResult.Error
        }
    }

    override fun getAllFavouritePlaces(): Flow<PagingData<Place>> =
        favouritePlacesDataSource.getAllFavouritePlaces()
}