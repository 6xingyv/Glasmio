package io.github.neoruaa.glasmio.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.github.neoruaa.glasmio.icons.SfIcon

@Composable
actual fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
) {
    SfIcon(
        systemName = systemName,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        size = size,
    )
}
