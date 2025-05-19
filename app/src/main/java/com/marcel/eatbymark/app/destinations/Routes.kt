package com.marcel.eatbymark.app.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_ROUTE = "home"

fun NavGraphBuilder.home(
    homeScreen: @Composable () -> Unit,
) = composable(HOME_ROUTE) {
    homeScreen()
}