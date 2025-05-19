package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.models.PlaceDish
import com.marcel.eatbymark.places.models.PlaceImage
import com.marcel.eatbymark.places.models.PlacePrice
import com.marcel.eatbymark.places.placesaround.data.models.GenericItemAPIResponse
import com.marcel.eatbymark.places.placesaround.data.models.VenuePreviewItemAPIResponse

fun GenericItemAPIResponse.toPlace(): Place? {
    return if (
        (venue?.id == null)
        || (venue.name == null)
        || (venue.short_description == null)
        || (image?.url == null)
        || (image.blurhash == null)
    ) null
    else Place(
        id = venue.id,
        dishes = venue.venue_preview_items?.mapNotNull { it.toPlaceDish() } ?: emptyList(),
        image = PlaceImage(image.blurhash, image.url),
        isFavourite = false,
        isOnline = venue.online == true,
        name = venue.name,
        shortDescription = venue.short_description,
        telemetryId = telemetry_object_id
    )
}

fun VenuePreviewItemAPIResponse.toPlaceDish(): PlaceDish? {
    return if (
        (id == null)
        || (name == null)
        || (image?.url == null)
        || (image.blurhash == null)
    ) null
    else {
        val dishPrice =
            if ((price != null) && (currency != null)) PlacePrice(currency, price) else null
        PlaceDish(
            id = id,
            image = PlaceImage(image.blurhash, image.url),
            name = name,
            price = dishPrice,
        )
    }
}