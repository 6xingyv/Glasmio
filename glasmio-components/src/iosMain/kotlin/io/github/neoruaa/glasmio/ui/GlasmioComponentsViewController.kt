package io.github.neoruaa.glasmio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.backdrop.rememberGlasmioBackdrop
import io.github.neoruaa.glasmio.backdrop.glasmioBackdropSource
import io.github.neoruaa.glasmio.backdrop.glasmioLiquidGlass
import io.github.neoruaa.glasmio.backdrop.GlasmioGlassPanel
import io.github.neoruaa.glasmio.backdrop.GlasmioGlassButton
import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlassColors
import platform.UIKit.UIViewController

/** UIKit entry point showing exactly the same Glasmio native controls as Desktop. */
fun GlasmioComponentsViewController(): UIViewController = ComposeUIViewController {
    var dark by remember { mutableStateOf(false) }
    GlasmioTheme(isDark = dark) {
        val colors = LocalGlassColors.current
        val backdrop = rememberGlasmioBackdrop()
        Box(Modifier.fillMaxSize().background(colors.groupedBackground)) {
            Box(Modifier.fillMaxSize().glasmioBackdropSource(backdrop)) {
                Canvas(Modifier.fillMaxSize()) {
                    drawRect(
                        Brush.linearGradient(
                            listOf(
                                if (dark) Color(0xFF253A58) else Color(0xFF96CCE2),
                                if (dark) Color(0xFF693854) else Color(0xFFF0C5E6),
                                if (dark) Color(0xFF1D2E3E) else Color(0xFFEAE2C6),
                            ),
                        ),
                    )
                }
            }
            Column(
                Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(24.dp),
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
            GlasmioGlassPanel(
                backdrop = backdrop,
                modifier = Modifier.fillMaxWidth().height(134.dp),
                material = NavigationGlassMaterial.Clear,
                sampleMinimumDimension = 134.dp,
                contentPadding = PaddingValues(20.dp),
            ) {
                GlasmioText(
                    "Shared GPU-refraction preview on iOS",
                    style = IosTypography.headline,
                    color = if (dark) Color.White else Color(0xFF1C1C1E),
                )
            }
            GlasmioGlassButton(
                backdrop = backdrop,
                label = "Toggle appearance",
                onClick = { dark = !dark },
            )
            GlasmioText(
                "Shared backdrop sampling works here; complete Android native-optics parity is not yet verified.",
                style = IosTypography.footnote,
            )
            }
        }
    }
}
