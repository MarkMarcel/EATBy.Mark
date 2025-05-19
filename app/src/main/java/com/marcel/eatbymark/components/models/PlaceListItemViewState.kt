package com.marcel.eatbymark.components.models

import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.models.PlaceDish

data class PlaceListItemViewState(
    val id: String,
    val dishes: List<PlaceListItemDishViewState>,
    val imageBlurHash: String,
    val imageUrl: String,
    val isExpanded: Boolean,
    val isFavourite: Boolean,
    val isOnline: Boolean,
    val name: String,
    val shortDescription: String,
    val telemetryId: String?,
    val onToggleExpanded: (id: String, isExpanded: Boolean, telemetryId: String?) -> Unit,
    val onToggleLikeStatus: (id: String, isLiked: Boolean, telemetryId: String?) -> Unit,
)

data class PlaceListItemDishViewState(
    val id: String,
    val currency: String?,
    val imageBlurHash: String,
    val imageUrl: String,
    val name: String,
    val price: Int?,
)

fun Place.toPlaceListItemViewState(
    isExpanded: Boolean,
    onToggleExpanded: (id: String, isExpanded: Boolean, telemetryId: String?) -> Unit,
    onToggleFavouriteStatus: (id: String, isFavourite: Boolean, telemetryId: String?) -> Unit,
) = PlaceListItemViewState(
    id = id,
    dishes = dishes.map { it.toPlaceListItemDishViewState() },
    imageBlurHash = image.blurHash,
    imageUrl = image.url,
    isExpanded = isExpanded,
    isFavourite = isFavourite,
    isOnline = isOnline,
    name = name,
    shortDescription = shortDescription,
    telemetryId = telemetryId,
    onToggleExpanded = onToggleExpanded,
    onToggleLikeStatus = onToggleFavouriteStatus,
)

fun PlaceDish.toPlaceListItemDishViewState() = PlaceListItemDishViewState(
    id = id,
    currency = price?.currency,
    imageBlurHash = image.blurHash,
    imageUrl = image.url,
    name = name,
    price = price?.value,
)

