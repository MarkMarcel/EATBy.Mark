package com.marcel.eatbymark.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.marcel.eatbymark.R
import com.marcel.eatbymark.core.toTitleCase
import com.marcel.eatbymark.places.favouriteplaces.ui.FavouritePlacesScreen
import com.marcel.eatbymark.places.placesaround.ui.PlacesAroundScreen

enum class Tabs {
    PlacesAround, FavouritePlaces
}

@Composable
fun HomeContent() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        Tabs.entries.size
    }
    Scaffold(
        topBar = {
            HomeTopBar(
                selectedTabIndex = selectedTabIndex,
            ) {
                selectedTabIndex = it
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { pageIndex ->
                when (pageIndex) {
                    Tabs.FavouritePlaces.ordinal -> FavouritePlacesScreen()
                    else -> PlacesAroundScreen()
                }
            }
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    TabRow(
        modifier = modifier.safeDrawingPadding(),
        selectedTabIndex = selectedTabIndex,
    ) {
        Tabs.entries.forEachIndexed { index, tab ->
            val tabName = when (tab) {
                Tabs.FavouritePlaces -> stringResource(R.string.favourite_places)
                Tabs.PlacesAround -> stringResource(R.string.places_around)
            }
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = tabName.toTitleCase(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    }
}