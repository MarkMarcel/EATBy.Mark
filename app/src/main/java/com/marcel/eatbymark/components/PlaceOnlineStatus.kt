package com.marcel.eatbymark.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.marcel.eatbymark.R
import com.marcel.eatbymark.core.toTitleCase
import com.marcel.eatbymark.theme.baseSpacingDiv4
import java.util.Locale

@Composable
fun PlaceOnlineIndicator(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.online_label).toTitleCase(Locale.getDefault()),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.width(baseSpacingDiv4))
        OnlineIndicator()
    }
}

@Composable
private fun OnlineIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 4.dp,
    color: Color = Color.Green,
    animationDurationMillis: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PlaceOnlineIndicatorAlpha")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMillis),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnimation"
    )

    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                val radius = this.size.minDimension / 2f
                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = radius,
                    center = Offset(radius, radius) // Center of the Box
                )
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun OnlineIndicatorPreview() {
    PlaceOnlineIndicator()
}