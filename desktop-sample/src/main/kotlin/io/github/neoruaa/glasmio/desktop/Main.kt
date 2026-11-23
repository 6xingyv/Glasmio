package io.github.neoruaa.glasmio.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.backdrop.rememberGlasmioBackdrop
import io.github.neoruaa.glasmio.backdrop.glasmioBackdropSource
import io.github.neoruaa.glasmio.backdrop.glasmioLiquidGlass
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
            val backdrop = rememberGlasmioBackdrop()
            Box(Modifier.fillMaxSize().background(colors.groupedBackground)) {
                // Draw the actual recorded backdrop behind the controls. This
                // layer is sampled on the GPU, not a preblurred static image.
                Box(Modifier.fillMaxSize().glasmioBackdropSource(backdrop)) {
                    Canvas(Modifier.fillMaxSize()) {
                        drawRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    if (isDark) Color(0xFF12283F) else Color(0xFFB8E2EB),
                                    if (isDark) Color(0xFF4B264C) else Color(0xFFF5C6E2),
                                    if (isDark) Color(0xFF1D2636) else Color(0xFFEDE0B8),
                                ),
                            ),
                        )
                        drawCircle(
                            color = Color(0xFF6EE7B7).copy(alpha = 0.7f),
                            radius = size.minDimension * .29f,
                            center = Offset(size.width * .72f, size.height * .31f),
                        )
                        drawCircle(
                            color = Color(0xFFD8B4FE).copy(alpha = 0.58f),
                            radius = size.minDimension * .24f,
                            center = Offset(size.width * .26f, size.height * .77f),
                        )
                    }
                }
                Column(
                    Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(32.dp),
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
                // This surface now performs GPU background capture, blur and
                // edge refraction through the common Gaze Glassy/Skiko backend.
                // Toggling the material switches real optical recipes.
                val selectedMaterial = if (clearMaterial) NavigationGlassMaterial.Clear
                    else NavigationGlassMaterial.RegularLarge
                Box(
                    Modifier.fillMaxWidth().height(152.dp)
                        .glasmioLiquidGlass(
                            backdrop = backdrop,
                            material = selectedMaterial,
                            sampleMinimumDimension = 152.dp,
                        )
                        .padding(24.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlasmioText(
                            "GPU-backed Liquid Glass",
                            style = IosTypography.title2,
                            color = if (isDark) Color.White else Color(0xFF1C1C1E),
                        )
                        GlasmioText(
                            "Captured background · ${selectedMaterial.name} · G2 edge refraction",
                            style = IosTypography.footnote,
                            color = if (isDark) Color.White else Color(0xFF1C1C1E),
                        )
                    }
                }
                GlasmioText("ChatKit-style message bubbles", style = IosTypography.title2)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlasmioMessageBubble(outgoing = false) { GlasmioText("Common UI on Desktop") }
                    GlasmioMessageBubble(outgoing = true) { GlasmioText("Same source on Android and iOS") }
                }
                GlasmioText(
                    "The common renderer performs backdrop capture and refraction. " +
                        "Full parity with Android's native C++/AGSL optics is not yet verified.",
                    style = IosTypography.footnote,
                    color = colors.secondaryContent,
                )
                }
            }
        }
    }
}
