package io.github.neoruaa.glasmio.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

/**
 * Desktop has no licensed SF Symbols font by default, so display explicit
 * Material vector equivalents. Applications can supply a more complete resolver.
 */
private fun desktopSymbol(systemName: String): ImageVector = when {
    systemName.startsWith("house") -> Icons.Default.Home
    systemName.startsWith("gear") -> Icons.Default.Settings
    systemName.startsWith("magnifyingglass") -> Icons.Default.Search
    systemName.startsWith("plus") -> Icons.Default.Add
    systemName.startsWith("minus") -> Icons.Default.Remove
    systemName.startsWith("xmark") -> Icons.Default.Close
    systemName.startsWith("heart") -> Icons.Default.Favorite
    systemName.startsWith("star") -> Icons.Default.Star
    systemName.startsWith("person") -> Icons.Default.Person
    systemName.startsWith("play") -> Icons.Default.PlayArrow
    systemName.startsWith("pause") -> Icons.Default.Pause
    systemName.startsWith("chevron.left") -> Icons.Default.ChevronLeft
    systemName.startsWith("chevron.right") -> Icons.Default.ChevronRight
    systemName.startsWith("chevron.down") -> Icons.Default.KeyboardArrowDown
    systemName.startsWith("chevron.up") -> Icons.Default.KeyboardArrowUp
    systemName.startsWith("arrow.backward") -> Icons.Default.ArrowBack
    systemName.startsWith("arrow.forward") -> Icons.Default.ArrowForward
    systemName.startsWith("trash") -> Icons.Default.Delete
    systemName.startsWith("pencil") -> Icons.Default.Edit
    systemName.startsWith("bell") -> Icons.Default.Notifications
    systemName.startsWith("calendar") -> Icons.Default.DateRange
    systemName.startsWith("photo") -> Icons.Default.Image
    systemName.startsWith("speaker") -> Icons.Default.VolumeUp
    else -> Icons.Default.Widgets
}

@Composable
actual fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
) {
    Icon(
        imageVector = desktopSymbol(systemName),
        contentDescription = contentDescription,
        modifier = modifier.then(Modifier.size(size)),
        tint = tint,
    )
}
