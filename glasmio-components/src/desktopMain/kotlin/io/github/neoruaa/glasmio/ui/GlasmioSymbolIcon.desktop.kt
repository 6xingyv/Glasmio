package io.github.neoruaa.glasmio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import io.github.neoruaa.glasmio.core.GlasmioBuiltInSymbols

/** Same non-Material Glasmio vector fallback used by Android when no symbol font is supplied. */
@Composable
actual fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
) {
    Image(
        imageVector = GlasmioBuiltInSymbols.resolve(systemName),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        colorFilter = ColorFilter.tint(tint),
    )
}
