package com.marcel.eatbymark.places.placesaround.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcel.eatbymark.R
import com.marcel.eatbymark.components.LoadingIndicator
import com.marcel.eatbymark.components.PlaceListItem
import com.marcel.eatbymark.components.models.PlaceListItemViewState
import com.marcel.eatbymark.theme.baseSpacing

@Composable
fun PlacesAroundScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesAroundScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    PlacesAroundScreenContent(
        modifier = modifier.fillMaxSize(),
        onIntent = viewModel::onIntent,
        stateProvider = { state }
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe lifecycle events
    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.onIntent(PlacesAroundScreenIntent.GetUpdates)
                }

                Lifecycle.Event.ON_STOP -> {
                    viewModel.onIntent(PlacesAroundScreenIntent.StopUpdates)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun PlacesAroundScreenContent(
    modifier: Modifier = Modifier,
    onIntent: (PlacesAroundScreenIntent) -> Unit,
    stateProvider: () -> PlacesAroundScreenViewState,
) {
    val state = stateProvider()
    Column(
        modifier.fillMaxSize()
    ) {
        AnimatedVisibility(state.isLoading) {
            PlacesAroundLoading()
        }
        AnimatedVisibility(state.message != null) {
            state.message?.let {
                PlacesAroundScreenMessage(
                    message = it,
                )
            }
        }
        PlacesAroundListItems(
            modifier = Modifier.weight(1f),
            items = state.places,
            expandedItemIndex = state.expandedItemIndex,
        )
    }
    LaunchedEffect(state.message) {
        if (state.message != null) {
            onIntent(PlacesAroundScreenIntent.MessageShown)
        }
    }
}

@Composable
private fun PlacesAroundListItems(
    modifier: Modifier = Modifier,
    expandedItemIndex: Int?,
    items: List<PlaceListItemViewState>,
) {
    val itemsState = rememberLazyListState()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = itemsState,
    ) {
        items(
            count = items.size,
            key = { index -> items[index].id },
        ) { index ->
            val item = items[index]
            PlaceListItem(
                modifier = Modifier.animateItem(),
                id = item.id,
                dishes = item.dishes,
                imageBlurHash = item.imageBlurHash,
                imageUrl = item.imageUrl,
                isExpanded = item.isExpanded,
                isFavourite = item.isFavourite,
                isOnline = item.isOnline,
                name = item.name,
                shortDescription = item.shortDescription,
                telemetryId = item.telemetryId,
                onToggleExpanded = item.onToggleExpanded,
                onToggleFavouriteStatus = item.onToggleLikeStatus,
            )
        }
    }
}

@Composable
private fun PlacesAroundLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        LoadingIndicator()
        Spacer(modifier = Modifier.height(baseSpacing))
        HorizontalDivider()
    }
}


@Composable
private fun PlacesAroundScreenMessage(
    modifier: Modifier = Modifier,
    message: PlacesAroundScreenMessage,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) {
            val text = when (message) {
                PlacesAroundScreenMessage.ApplicationError -> stringResource(R.string.application_error)
                PlacesAroundScreenMessage.FavouritePlaceToggleError -> stringResource(R.string.favourite_place_toggle_error)
                PlacesAroundScreenMessage.NetworkError -> stringResource(R.string.network_error)
                PlacesAroundScreenMessage.UnknownError -> stringResource(R.string.unknown_error)
            }
            Text(text.capitalize(Locale.current))
        }
        Spacer(modifier = Modifier.height(baseSpacing))
        HorizontalDivider()
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PlacesAroundScreenPreview() {
    PlacesAroundScreenContent(
        onIntent = {}
    )
}*/
