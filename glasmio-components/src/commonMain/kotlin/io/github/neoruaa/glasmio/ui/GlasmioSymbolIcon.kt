package io.github.neoruaa.glasmio.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.LocalGlassColors

/**
 * System symbol renderer shared across Android, Desktop and iOS.
 *
 * Android delegates to the existing SfIcon pipeline; iOS uses UIKit SF Symbols;
 * Desktop supplies Material vector fallbacks without redistributing Apple fonts.
 * Symbol names are taken from SfSymbolCatalog, which may include symbols absent
 * from older iOS versions.
 */
@Composable
expect fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalGlassColors.current.content,
    size: Dp = 24.dp,
)
