package com.marcel.eatbymark

import android.content.ComponentCallbacks2
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.marcel.eatbymark.app.HomeContent
import com.marcel.eatbymark.app.destinations.HOME_ROUTE
import com.marcel.eatbymark.app.destinations.home
import com.marcel.eatbymark.components.BlurHashDecoder
import com.marcel.eatbymark.theme.EATByMarkTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity(), ComponentCallbacks2 {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //throw Exception("test")
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            EATByMarkTheme {
                SystemBarIconController(true)
                NavHost(
                    navController = navController,
                    startDestination = HOME_ROUTE
                ){
                    home { HomeContent() }
                }
            }
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_RUNNING_LOW) {
            BlurHashDecoder.clearCache()
        }
    }
}

@Composable
fun SystemBarIconController(useDarkIcons: Boolean) {
    val view = LocalView.current
    // DisposableEffect ensures that the original icon style is restored
    // when the composable leaves the composition, though for an Activity-level
    // setting like this, it might not be strictly necessary if you only set it once.
    // However, it's good practice if this were a screen-specific setting.
    DisposableEffect(useDarkIcons) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            val insetsController = WindowCompat.getInsetsController(it, view)
            insetsController.isAppearanceLightStatusBars = useDarkIcons
            insetsController.isAppearanceLightNavigationBars = useDarkIcons
        }
        onDispose {
            // Optionally restore original settings if needed,
            // but for an app-wide setting, this might not be necessary.
        }
    }
}