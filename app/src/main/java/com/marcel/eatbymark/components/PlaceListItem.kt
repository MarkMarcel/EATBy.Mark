package com.marcel.eatbymark.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcel.eatbymark.R
import com.marcel.eatbymark.components.models.PlaceListItemDishViewState
import com.marcel.eatbymark.components.models.PlaceListItemViewState
import com.marcel.eatbymark.core.toTitleCase
import com.marcel.eatbymark.theme.baseSpacing
import com.marcel.eatbymark.theme.baseSpacingDiv2
import com.marcel.eatbymark.theme.baseSpacingDiv4
import java.util.Locale

@Composable
fun PlaceListItem(
    modifier: Modifier = Modifier,
    id: String,
    dishes: List<PlaceListItemDishViewState>,
    isExpanded: Boolean,
    imageBlurHash: String,
    imageUrl: String,
    isFavourite: Boolean,
    isOnline: Boolean,
    name: String,
    shortDescription: String,
    telemetryId: String?,
    onToggleExpanded: (id: String, isExpanded: Boolean, telemetryId: String?) -> Unit,
    onToggleFavouriteStatus: (id: String, isLiked: Boolean, telemetryId: String?) -> Unit,
) {
    Column(
        modifier = modifier.clickable {
            onToggleExpanded(id, !isExpanded, telemetryId)
        }
    ) {
        PlaceListItemExpandableContent(
            id = id,
            dishes = dishes,
            imageBlurHash = imageBlurHash,
            imageUrl = imageUrl,
            isExpanded = isExpanded,
            isFavourite = isFavourite,
            isOnline = isOnline,
            name = name,
            shortDescription = shortDescription,
            telemetryId = telemetryId,
            onToggleFavouriteStatus = onToggleFavouriteStatus,
        )
        Spacer(modifier = Modifier.height(baseSpacing))
        HorizontalDivider()
    }
}

@Composable
private fun PlaceListItemExpandableContent(
    modifier: Modifier = Modifier,
    dishes: List<PlaceListItemDishViewState>,
    id: String,
    imageBlurHash: String,
    imageUrl: String,
    isExpanded: Boolean,
    isFavourite: Boolean,
    isOnline: Boolean,
    name: String,
    shortDescription: String,
    telemetryId: String?,
    onToggleFavouriteStatus: (id: String, isLiked: Boolean, telemetryId: String?) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        PlaceListItemFixedContent(
            id = id,
            imageBlurHash = imageBlurHash,
            imageUrl = imageUrl,
            isFavourite = isFavourite,
            isOnline = isOnline,
            name = name,
            shortDescription = shortDescription,
            telemetryId = telemetryId,
            onToggleFavouriteStatus = onToggleFavouriteStatus,
        )
        if (isExpanded) {
            Spacer(modifier = Modifier.height(baseSpacing))
            PlaceListItemExpandedContent(
                dishes = dishes,
            )
        }
    }
}

@Composable
private fun PlaceListItemFixedContent(
    modifier: Modifier = Modifier,
    id: String,
    imageBlurHash: String,
    imageUrl: String,
    isFavourite: Boolean,
    isOnline: Boolean,
    name: String,
    shortDescription: String,
    telemetryId: String?,
    onToggleFavouriteStatus: (id: String, isLiked: Boolean, telemetryId: String?) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = baseSpacing,
                end = baseSpacing,
                top = baseSpacing,
            ),
    ) {
        Column {
            PlaceListItemImage(
                blurHash = imageBlurHash,
                imageUrl = imageUrl,
            )
        }
        Spacer(modifier = Modifier.width(baseSpacing))
        PlaceListItemInfo(
            modifier = Modifier.weight(1f),
            isOnline = isOnline,
            name = name,
            shortDescription = shortDescription,
        )
        Spacer(modifier = Modifier.width(baseSpacing))
        PlaceListItemFavouriteStatus(
            isFavourite = isFavourite,
            placeId = id,
            placeName = name,
            telemetryId = telemetryId,
            onToggleFavoriteStatus = onToggleFavouriteStatus,
        )
    }
}


@Composable
private fun PlaceListItemExpandedContent(
    modifier: Modifier = Modifier,
    dishes: List<PlaceListItemDishViewState>,
) {
    Box(modifier) {
        if (dishes.isNotEmpty()) {
            PlaceListItemDishes(
                dishes = dishes,
            )
        }
    }
}

@Composable
private fun PlaceListItemDishes(
    modifier: Modifier = Modifier,
    dishes: List<PlaceListItemDishViewState>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(Modifier.padding(horizontal = baseSpacing)) {
            Text(
                stringResource(R.string.dishes_title).toTitleCase(Locale.getDefault()),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Spacer(modifier = Modifier.height(baseSpacing))
        LazyRow() {
            items(count = dishes.size, key = { dishes[it].id }) {
                PlaceListItemDish(
                    currency = dishes[it].currency,
                    imageBlurHash = dishes[it].imageBlurHash,
                    imageUrl = dishes[it].imageUrl,
                    name = dishes[it].name,
                    price = dishes[it].price,
                )
            }
        }
    }
}

@Composable
private fun PlaceListItemImage(
    modifier: Modifier = Modifier,
    blurHash: String,
    imageUrl: String,
) {
    Box(
        modifier
            .requiredSize(60.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        // Leave contentDescription null as this has completely visual role
        BlurHashImage(
            modifier = Modifier.matchParentSize(),
            imageUrl = imageUrl,
            blurHash = blurHash,
        )
    }
}

@Composable
private fun PlaceListItemInfo(
    modifier: Modifier = Modifier,
    isOnline: Boolean,
    name: String,
    shortDescription: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        PlaceListItemName(
            name = name,
        )
        if (isOnline) {
            Spacer(modifier = Modifier.height(baseSpacingDiv4))
            PlaceOnlineIndicator()
        }
        Spacer(modifier = Modifier.height(baseSpacingDiv2))
        PlaceListItemShortDescription(
            shortDescription = shortDescription,
        )
    }
}

@Composable
private fun PlaceListItemFavouriteStatus(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    placeId: String,
    placeName: String,
    telemetryId: String?,
    onToggleFavoriteStatus: (id: String, isLiked: Boolean, telemetryId: String?) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                onToggleFavoriteStatus(placeId, !isFavourite, telemetryId)
            }
        ) {
            if (isFavourite) {
                AnimatedLikedIcon(
                    contentDescription = stringResource(
                        R.string.place_liked_icon_content_description,
                        placeName,
                    ),
                )
            } else {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(
                        R.string.place_not_liked_icon_content_description,
                        placeName,
                    ),
                )
            }
        }
    }
}

@Composable
private fun PlaceListItemName(
    modifier: Modifier = Modifier,
    name: String,
) {
    Text(
        name,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun PlaceListItemShortDescription(
    modifier: Modifier = Modifier,
    shortDescription: String,
) {
    Text(
        shortDescription,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview
@Composable
fun PlaceListItemPreview() {
    val state = remember {
        listOf(
            PlaceListItemViewState(
                id = "66bca88cff8d9039f5819859",
                dishes = emptyList(),
                imageBlurHash = "LEHV6nWB2yk8pyo0adR*.7kCMdnj",
                imageUrl = "https://imageproxy.wolt.com/assets/67335e99e01243558b0c0c5f",
                isExpanded = false,
                isFavourite = false,
                isOnline = true,
                name = "The Bro's Burger Herttoniemi",
                shortDescription = "Juicy burgers!",
                telemetryId = null,
                onToggleExpanded = { itemId, newExpandedState, telemetryId ->
                },
                onToggleLikeStatus = { itemId, newStatus, telemetryId ->
                }
            ),
            PlaceListItemViewState(
                id = "66bca88cff8d9039f5819859",
                dishes = emptyList(),
                imageBlurHash = "jiTsyRTKLuF3X;GXh4tlTtl4p3Lc",
                imageUrl = "https://imageproxy.wolt.com/assets/67335e99e01243558b0c0c5f",
                isExpanded = false,
                isFavourite = true,
                isOnline = false,
                name = "The Bro's Burger Herttoniemi",
                shortDescription = "Juicy burgers!",
                telemetryId = null,
                onToggleExpanded = { itemId, newExpandedState, telemetryId ->
                },
                onToggleLikeStatus = { itemId, newStatus, telemetryId ->
                }
            )
        )
    }
    Surface(
        modifier = Modifier.padding(
            WindowInsets(
                0.dp,
                32.dp,
                0.dp,
                WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 32.dp
            ).asPaddingValues()
        ),
    ) {
        Column {
            PlaceListItem(
                id = state[0].id,
                dishes = state[0].dishes,
                imageBlurHash = state[0].imageBlurHash,
                imageUrl = state[0].imageUrl,
                isExpanded = false,
                isFavourite = state[0].isFavourite,
                isOnline = true,
                name = state[0].name,
                shortDescription = state[0].shortDescription,
                telemetryId = state[0].telemetryId,
                onToggleExpanded = state[0].onToggleExpanded,
                onToggleFavouriteStatus = state[0].onToggleLikeStatus,
            )
            Spacer(modifier = Modifier.height(baseSpacing))
            PlaceListItem(
                id = state[1].id,
                dishes = state[1].dishes,
                imageBlurHash = state[1].imageBlurHash,
                imageUrl = state[1].imageUrl,
                isExpanded = false,
                isFavourite = state[1].isFavourite,
                isOnline = false,
                name = state[1].name,
                shortDescription = state[1].shortDescription,
                telemetryId = state[1].telemetryId,
                onToggleExpanded = state[1].onToggleExpanded,
                onToggleFavouriteStatus = state[1].onToggleLikeStatus,
            )
        }
    }
}