package com.marcel.eatbymark.places.placesaround

data class PlacesAroundConfig(
    val pageSize: Int = 15,
    val updatesIntervalSeconds: Long = 10,
)