package com.marcel.eatbymark.core

import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig

data class AppConfig(
    val databaseConfig: DatabaseConfig,
    val networkClientConfig: NetworkClientConfig,
    val placesAroundConfig: PlacesAroundConfig,
)
