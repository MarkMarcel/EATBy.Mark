package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.places.placesaround.data.models.PlacesAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesAroundAPI {
    @GET("restaurants")
    suspend fun getPlacesAround(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): PlacesAPIResponse
}