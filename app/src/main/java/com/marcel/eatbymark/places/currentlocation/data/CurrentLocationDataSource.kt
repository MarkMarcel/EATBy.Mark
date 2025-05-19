package com.marcel.eatbymark.places.currentlocation.data

import com.marcel.eatbymark.places.models.Coordinate
import kotlinx.coroutines.flow.Flow

interface CurrentLocationDataSource {
    fun getCurrentLocation(updatesIntervalSeconds: Long): Flow<Coordinate>
}