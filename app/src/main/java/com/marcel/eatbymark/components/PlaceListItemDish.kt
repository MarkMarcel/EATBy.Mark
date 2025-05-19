package com.marcel.eatbymark.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marcel.eatbymark.theme.baseSpacing
import com.marcel.eatbymark.theme.baseSpacingDiv2
import com.marcel.eatbymark.theme.baseSpacingDiv4

@Composable
fun PlaceListItemDish(
    modifier: Modifier = Modifier,
    currency: String?,
    imageBlurHash: String,
    imageUrl: String,
    name: String,
    price: Int?,
) {
    Box(
        modifier = modifier.padding(baseSpacing)
    ) {
        Box(
            Modifier.requiredWidth(100.dp)
        ) {
            Column {
                DishImage(
                    blurHash = imageBlurHash,
                    imageUrl = imageUrl,
                )
                Spacer(modifier = Modifier.height(baseSpacingDiv2))
                DishInfo(
                    currency = currency,
                    name = name,
                    price = price,
                )
            }
        }
    }
}

@Composable
private fun DishImage(
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
private fun DishInfo(
    modifier: Modifier = Modifier,
    currency: String?,
    name: String,
    price: Int?,
) {
    Column(modifier = modifier) {
        DishName(
            name = name,
        )
        Spacer(modifier = Modifier.height(baseSpacingDiv4))
        if((currency != null) && (price != null)){
            DishPrice(
                currency = currency,
                price = price,
            )
        }
    }
}

@Composable
private fun DishName(
    modifier: Modifier = Modifier,
    name: String,
) {
    Text(
        name,
        modifier = modifier.fillMaxWidth(),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
private fun DishPrice(
    modifier: Modifier = Modifier,
    currency: String,
    price: Int,
) {
    val priceString = remember(currency, price) {
        "$currency $price"
    }
    Text(
        priceString,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
    )
}