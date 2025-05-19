package com.marcel.eatbymark.places.currentlocation.data

import android.util.Log
import com.marcel.eatbymark.core.BackgroundDispatcher
import com.marcel.eatbymark.places.models.Coordinate
import com.marcel.eatbymark.places.models.coordinates
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

class DefaultCurrentLocationDataSource @Inject constructor(
    @BackgroundDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : CurrentLocationDataSource {
    /* Lifecycle of this flow is tied to the application as it essentially is simulating device
    * location, which is always on */
    override fun getCurrentLocation(
        updatesIntervalSeconds: Long
    ): Flow<Coordinate> {
        return flow {
            var currentLocationIndex = 0
            while (currentCoroutineContext().isActive) {
                emit(coordinates[currentLocationIndex])
                currentLocationIndex = (currentLocationIndex + 1) % coordinates.size
                delay(updatesIntervalSeconds * 1000)
            }
        }.flowOn(coroutineDispatcher)
    }
}