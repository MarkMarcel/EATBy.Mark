package com.marcel.eatbymark.places.favouriteplaces.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.marcel.eatbymark.R
import com.marcel.eatbymark.components.LoadingIndicator
import com.marcel.eatbymark.components.PlaceListItem
import com.marcel.eatbymark.components.models.PlaceListItemViewState
import com.marcel.eatbymark.theme.baseSpacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
fun FavouritePlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouritePlacesScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    FavouritePlacesScreenContent(
        modifier = modifier.fillMaxSize(),
        onIntent = viewModel::onIntent,
        itemsProvider = { viewModel.viewState.map { state -> state.places } },
        stateProvider = { state }
    )
}

@Composable
private fun FavouritePlacesScreenContent(
    modifier: Modifier = Modifier,
    onIntent: (FavouritePlacesScreenIntent) -> Unit,
    itemsProvider: () -> Flow<PagingData<PlaceListItemViewState>>,
    stateProvider: () -> FavouritePlacesScreenViewState,
) {
    val state = stateProvider()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(state.isLoading) {
            FavouritePlacesLoading()
        }
        AnimatedVisibility(state.message != null) {
            state.message?.let {
                FavouritePlacesScreenMessage(
                    message = it,
                )
            }
        }
        FavouritePlacesListItems(
            modifier = Modifier.weight(1f),
            itemsProvider = itemsProvider,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun FavouritePlacesLoading(
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
private fun FavouritePlacesListItems(
    modifier: Modifier = Modifier,
    itemsProvider: () -> Flow<PagingData<PlaceListItemViewState>>,
    onIntent: (FavouritePlacesScreenIntent) -> Unit,
) {
    val items = itemsProvider().collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id ?: "" },
        ) { index ->
            val item = items[index]
            if (item != null) {
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
    PageLoadStateObserver(
        pagedState = items,
        onIntent = onIntent,
    )
    PageLoadErrorObserver(
        pagedState = items,
        onIntent = onIntent,
    )
}

@Composable
private fun FavouritePlacesScreenMessage(
    modifier: Modifier = Modifier,
    message: FavouritePlacesScreenMessage,
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
                FavouritePlacesScreenMessage.ApplicationError -> stringResource(R.string.application_error)
                FavouritePlacesScreenMessage.FavouritePlaceToggleError -> stringResource(R.string.favourite_place_toggle_error)
                FavouritePlacesScreenMessage.UnknownError -> stringResource(R.string.unknown_error)
            }
            Text(text.capitalize(Locale.current))
        }
        Spacer(modifier = Modifier.height(baseSpacing))
        HorizontalDivider()
    }
}


@Composable
private fun PageLoadErrorObserver(
    pagedState: LazyPagingItems<PlaceListItemViewState>,
    onIntent: (FavouritePlacesScreenIntent) -> Unit,
) {
    LaunchedEffect(pagedState.loadState) {
        if (pagedState.loadState.hasError) {
            onIntent(FavouritePlacesScreenIntent.PageError)
        }
    }
}


@Composable
private fun PageLoadStateObserver(
    pagedState: LazyPagingItems<PlaceListItemViewState>,
    onIntent: (FavouritePlacesScreenIntent) -> Unit,
) {
    LaunchedEffect(pagedState.loadState) {
        if (
            ((pagedState.loadState.refresh == LoadState.Loading)
                    || (pagedState.loadState.append == LoadState.Loading))
        ) {
            onIntent(FavouritePlacesScreenIntent.PageLoading)
        } else {
            onIntent(FavouritePlacesScreenIntent.PageLoaded)
        }
    }
}