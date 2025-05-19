package com.marcel.eatbymark.places.models

data class Place(
    val id: String,
    val dishes: List<PlaceDish>,
    val image: PlaceImage,
    val isFavourite: Boolean,
    val isOnline: Boolean,
    val name: String,
    val shortDescription: String,
    val telemetryId: String?,
)

data class PlaceDish(
    val id: String,
    val name: String,
    val image: PlaceImage,
    val price: PlacePrice?,
)

data class PlaceImage(
    val blurHash: String,
    val url: String,
)

data class PlacePrice(
    val currency: String,
    val value: Int,
)
