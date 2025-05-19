package com.marcel.eatbymark.places.favouriteplaces.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.marcel.eatbymark.core.IODispatcher
import com.marcel.eatbymark.places.favouriteplaces.domain.models.FavouritePlacesUpdateResult
import com.marcel.eatbymark.places.favouriteplaces.domain.models.GetFavouritePlacesIdsResult
import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavouritePlacesLocalDataSource @Inject constructor(
    private val favouritePlacesDao: FavouritePlacesDAO,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val placesAroundConfig: PlacesAroundConfig,
) : FavouritePlacesDataSource {
    override suspend fun addFavouritePlace(place: Place): FavouritePlacesUpdateResult {
        return withContext(ioDispatcher) {
            try {
                favouritePlacesDao.addFavouritePlace(place.toFavouritePlaceDatabaseModel())
                FavouritePlacesUpdateResult.Success
            } catch (e: Exception) {
                FavouritePlacesUpdateResult.Error
            }
        }
    }

    override suspend fun removeFavouritePlace(placeId: String): FavouritePlacesUpdateResult {
        return withContext(ioDispatcher) {
            try {
                favouritePlacesDao.removeFavouritePlace(placeId)
                FavouritePlacesUpdateResult.Success
            } catch (e: Exception) {
                FavouritePlacesUpdateResult.Error
            }
        }
    }

    override fun getAllFavouritePlaces(): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(
                pageSize = placesAroundConfig.pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { favouritePlacesDao.getAllFavouritePlaces() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { placeData -> placeData.toPlace() }
            }
            .flowOn(ioDispatcher)
    }

    override fun getAllFavouritePlacesIds(): Flow<GetFavouritePlacesIdsResult> {
        return favouritePlacesDao.getAllFavouritePlacesIds()
            .map { ids -> GetFavouritePlacesIdsResult.Success(ids) }
            .catch { e -> GetFavouritePlacesIdsResult.Error }
            .flowOn(ioDispatcher)
    }
}