package io.github.neoruaa.glasmio.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.neoruaa.glasmio.core.GlasmioTheme
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlasmioShapes
import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial
import io.github.neoruaa.glasmio.glass.navigationGlassGeometryProfile
import io.github.neoruaa.glasmio.glass.render.optics.GlassOptics
import io.github.neoruaa.glasmio.icons.SfSymbolCatalog
import io.github.neoruaa.glasmio.ui.GlasmioMessageBubble
import io.github.neoruaa.glasmio.ui.GlasmioSidebarToggleIcon

/** A buildable Desktop consumer of the portable modules, NOT a Liquid Glass renderer. */
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Glasmio KMP - Desktop foundation") {
        var isDark by remember { mutableStateOf(false) }
        var clearMaterial by remember { mutableStateOf(false) }
        GlasmioTheme(isDark = isDark) {
            val shape = LocalGlasmioShapes.current.medium
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(Modifier.padding(32.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        GlasmioSidebarToggleIcon()
                        Text("Glasmio", style = IosTypography.largeTitle)
                    }
                    Text("Compose Multiplatform foundation: shared tokens, SF Symbols, motion and optics.")
                    Card(shape = shape) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row {
                                Text("Dark appearance", modifier = Modifier.weight(1f))
                                Switch(checked = isDark, onCheckedChange = { isDark = it })
                            }
                            Row {
                                Text("Clear material profile", modifier = Modifier.weight(1f))
                                Switch(checked = clearMaterial, onCheckedChange = { clearMaterial = it })
                            }
                            val material = if (clearMaterial) NavigationGlassMaterial.Clear
                                else NavigationGlassMaterial.RegularLarge
                            val geometry = navigationGlassGeometryProfile(150f, material)
                            Text("Native blur radius: ${geometry.blurRadius} pt")
                            Text("Reference displacement: ${GlassOptics.displacement(-5f, .1f, 14f)}")
                            Text("Registered symbol addresses: ${SfSymbolCatalog.systemNames.size}")
                            Row {
                                Button(onClick = { clearMaterial = !clearMaterial }) { Text("Toggle material") }
                                Spacer(Modifier.width(8.dp))
                                Text("Optical profile only - no backdrop sampling", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Text("Shared ChatKit-style message bubbles", style = IosTypography.title2)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GlasmioMessageBubble(outgoing = false) {
                            Text("Rendered by commonMain on Desktop")
                        }
                        GlasmioMessageBubble(outgoing = true) {
                            Text("Identical shape and layout APIs on Android and iOS")
                        }
                    }
                    Text(
                        "This Desktop preview intentionally does not simulate Android's RuntimeShader/JNI " +
                            "glass pipeline. A Skia/Metal renderer remains necessary for visual parity.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
