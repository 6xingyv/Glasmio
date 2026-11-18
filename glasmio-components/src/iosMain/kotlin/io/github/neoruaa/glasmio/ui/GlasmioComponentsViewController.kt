package io.github.neoruaa.glasmio.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.core.IosTypography
import platform.UIKit.UIViewController

/**
 * UIKit entry point for the actual shared Compose controls. This does not use
 * Android's RenderEffect/RuntimeShader backend or claim Liquid Glass parity.
 *
 * Swift: GlasmioComponentsViewControllerKt.GlasmioComponentsViewController()
 */
fun GlasmioComponentsViewController(): UIViewController = ComposeUIViewController {
    var dark by remember { mutableStateOf(false) }
    GlasmioTheme(isDark = dark) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.windowInsetsPadding(WindowInsets.safeDrawing).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GlasmioSidebarToggleIcon()
                    GlasmioSymbolIcon("house.fill", "Home")
                    Text("Glasmio", style = IosTypography.largeTitle)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Dark appearance", style = IosTypography.body)
                    Switch(checked = dark, onCheckedChange = { dark = it })
                }
                GlasmioMessageBubble(outgoing = false) {
                    Text("Compose Multiplatform controls rendered on iOS.")
                }
                GlasmioMessageBubble(outgoing = true) {
                    Text("Shared message contours, text styles and interaction state.")
                }
                Text(
                    "The native Liquid Glass rendering backend is still Android-only.",
                    style = IosTypography.footnote,
                )
            }
        }
    }
}
