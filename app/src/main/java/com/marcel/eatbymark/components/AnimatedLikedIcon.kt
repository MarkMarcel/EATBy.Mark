package com.marcel.eatbymark.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLikedIcon(
    modifier: Modifier = Modifier,
    contentDescription: String,
    iconSizeDp: Int = 24
) {
    val animatedTransitionState =
        remember { MutableTransitionState(0) }
    val animatedTransition = rememberTransition(
        transitionState = animatedTransitionState,
        label = "Liked Icon Transition"
    )
    val size by animatedTransition.animateDp { requested ->
        if (requested == 0)
            0.dp
        else
            iconSizeDp.dp
    }
    Icon(
        Icons.Filled.Favorite,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier.requiredSize(size)
    )
    LaunchedEffect(true) {
        animatedTransitionState.targetState = iconSizeDp
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x000000)
@Composable
fun AnimatedLikedIconPreview() {
    AnimatedLikedIcon(
        modifier = Modifier.padding(
            WindowInsets(
                16.dp,
                32.dp,
                16.dp,
                WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 32.dp
            ).asPaddingValues()
        ),
        contentDescription = "Liked"
    )
}