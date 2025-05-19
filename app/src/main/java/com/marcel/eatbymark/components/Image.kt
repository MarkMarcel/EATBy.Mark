package com.marcel.eatbymark.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BlurHashImage(
    modifier: Modifier = Modifier,
    blurHash: String,
    imageUrl: String,
    contentDescription: String? = null,
    width: Int = 20,   // Low-res preview size
    height: Int = 20,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var placeholderPainter: Painter? by remember(blurHash, width, height) { mutableStateOf(null) }

    // Launch effect to decode blurhash and update painter
    LaunchedEffect(blurHash, width, height) {
        placeholderPainter = try {
            withContext(Dispatchers.IO) { // Perform decode on a background thread
                BlurHashDecoder.decode(blurHash, width, height)
                    ?.asImageBitmap()
                    ?.let { BitmapPainter(it) }
            }
        } catch (e: Exception) {
            null
        }
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        placeholder = placeholderPainter,
        error = placeholderPainter, // show blur hash on error too
        contentScale = contentScale,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x000000)
@Composable
fun BlurHashImagePreview() {
    BlurHashImage(
        imageUrl = "https://discovery-cdn.wolt.cm/categories/fd1d18e0-c5a8-11ea-8a78-822e244794a0_a1852dc9_4afb_4877_9659_793adcb0f87b.jpg-md",
        blurHash = "UaO}J3jF}[fkbHbHj[jZ}[bHR*fkoKjZWCfQ",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}
