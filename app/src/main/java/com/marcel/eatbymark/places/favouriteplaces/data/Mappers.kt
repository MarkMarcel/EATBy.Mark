package com.marcel.eatbymark.places.favouriteplaces.data

import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.models.PlaceImage

fun FavouritePlaceDatabaseModel.toPlace() = Place(
    id = id,
    dishes = emptyList(),
    image = PlaceImage(imageBlurHash, imageUrl),
    isFavourite = isFavourite,
    isOnline = false,
    name = name,
    shortDescription = shortDescription,
    telemetryId = telemetryId
)

fun Place.toFavouritePlaceDatabaseModel() = FavouritePlaceDatabaseModel(
    id = id,
    imageBlurHash = image.blurHash,
    imageUrl = image.url,
    isFavourite = isFavourite,
    name = name,
    shortDescription = shortDescription,
    telemetryId = telemetryId
)