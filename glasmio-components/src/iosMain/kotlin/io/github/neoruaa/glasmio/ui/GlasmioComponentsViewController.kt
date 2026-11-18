package io.github.neoruaa.glasmio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlassColors
import platform.UIKit.UIViewController

/** UIKit entry point showing exactly the same Glasmio native controls as Desktop. */
fun GlasmioComponentsViewController(): UIViewController = ComposeUIViewController {
    var dark by remember { mutableStateOf(false) }
    GlasmioTheme(isDark = dark) {
        val colors = LocalGlassColors.current
        Column(
            Modifier.fillMaxSize().background(colors.groupedBackground)
                .windowInsetsPadding(WindowInsets.safeDrawing).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GlasmioSidebarToggleIcon()
                GlasmioSymbolIcon("house.fill", "Home")
                GlasmioText("Glasmio", style = IosTypography.largeTitle)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GlasmioText("Dark appearance", style = IosTypography.body)
                GlasmioNativeSwitch(dark, onCheckedChange = { dark = it })
            }
            GlasmioMessageBubble(outgoing = false) {
                GlasmioText("Compose Multiplatform controls rendered on iOS.")
            }
            GlasmioMessageBubble(outgoing = true) {
                GlasmioText("Shared message contours, text styles and interaction state.")
            }
            GlasmioText(
                "The native backdrop refraction backend is still Android-only.",
                style = IosTypography.footnote,
            )
        }
    }
}
