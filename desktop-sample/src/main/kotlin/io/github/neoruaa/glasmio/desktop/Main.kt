package io.github.neoruaa.glasmio.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlassColors
import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial
import io.github.neoruaa.glasmio.glass.navigationGlassGeometryProfile
import io.github.neoruaa.glasmio.glass.render.optics.GlassOptics
import io.github.neoruaa.glasmio.icons.SfSymbolCatalog
import io.github.neoruaa.glasmio.ui.GlasmioMessageBubble
import io.github.neoruaa.glasmio.ui.GlasmioSidebarToggleIcon
import io.github.neoruaa.glasmio.ui.GlasmioSymbolIcon
import io.github.neoruaa.glasmio.ui.GlasmioNativePanel
import io.github.neoruaa.glasmio.ui.GlasmioNativeButton
import io.github.neoruaa.glasmio.ui.GlasmioNativeSwitch

/** All Desktop controls are pure Glasmio/Compose Foundation, never Material. */
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Glasmio KMP - Desktop") {
        var isDark by remember { mutableStateOf(false) }
        var clearMaterial by remember { mutableStateOf(false) }
        GlasmioTheme(isDark = isDark) {
            val colors = LocalGlassColors.current
            Column(
                Modifier.fillMaxSize().background(colors.groupedBackground)
                    .verticalScroll(rememberScrollState()).padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GlasmioSidebarToggleIcon()
                    GlasmioText("Glasmio", style = IosTypography.largeTitle)
                    GlasmioSymbolIcon("house.fill", "Home")
                }
                GlasmioText("Shared Glasmio tokens, SF glyph names, motion, and optical definitions.")
                GlasmioNativePanel {
                    Row {
                        GlasmioText("Dark appearance", Modifier.weight(1f))
                        GlasmioNativeSwitch(isDark, onCheckedChange = { isDark = it })
                    }
                    Row {
                        GlasmioText("Clear material profile", Modifier.weight(1f))
                        GlasmioNativeSwitch(clearMaterial, onCheckedChange = { clearMaterial = it })
                    }
                    val material = if (clearMaterial) NavigationGlassMaterial.Clear else NavigationGlassMaterial.RegularLarge
                    val geometry = navigationGlassGeometryProfile(150f, material)
                    GlasmioText("Native blur radius: ${geometry.blurRadius} pt")
                    GlasmioText("Reference displacement: ${GlassOptics.displacement(-5f, .1f, 14f)}")
                    GlasmioText("Registered symbol addresses: ${SfSymbolCatalog.systemNames.size}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlasmioNativeButton("Toggle material", onClick = { clearMaterial = !clearMaterial })
                        Spacer(Modifier.width(8.dp))
                        GlasmioText("Optical profile only", style = IosTypography.footnote)
                    }
                }
                GlasmioText("ChatKit-style message bubbles", style = IosTypography.title2)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlasmioMessageBubble(outgoing = false) { GlasmioText("Common UI on Desktop") }
                    GlasmioMessageBubble(outgoing = true) { GlasmioText("Same source on Android and iOS") }
                }
                GlasmioText(
                    "Desktop backdrop capture and refraction still require a native GPU backend; " +
                        "no visually unrelated fallback is substituted.",
                    style = IosTypography.footnote,
                    color = colors.secondaryContent,
                )
            }
        }
    }
}
