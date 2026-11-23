package io.github.neoruaa.glasmio.backdrop

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.LocalGlassAppearance
import io.github.neoruaa.glasmio.core.LocalGlassColors
import io.github.neoruaa.glasmio.core.LocalGlasmioShapes
import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial
import io.github.neoruaa.glasmio.glass.navigationGlassGeometryProfile
import com.mocharealm.gaze.glassy.liquid.effect.Backdrop
import com.mocharealm.gaze.glassy.liquid.effect.backdrops.LayerBackdrop
import com.mocharealm.gaze.glassy.liquid.effect.backdrops.layerBackdrop
import com.mocharealm.gaze.glassy.liquid.effect.backdrops.rememberLayerBackdrop
import com.mocharealm.gaze.glassy.liquid.effect.drawBackdrop
import com.mocharealm.gaze.glassy.liquid.effect.effects.blur
import com.mocharealm.gaze.glassy.liquid.effect.effects.lens

/**
 * Capture the content BEHIND a glass control once, so Skiko can sample it on
 * Desktop/iOS and Android can use the same optically calibrated inputs.
 *
 * Place [glasmioBackdropSource] on the background, never on the foreground
 * overlay: capturing the overlay would recursively capture the glass itself.
 */
@Composable
fun rememberGlasmioBackdrop(): LayerBackdrop = rememberLayerBackdrop()

fun Modifier.glasmioBackdropSource(backdrop: LayerBackdrop): Modifier =
    this.layerBackdrop(backdrop)

/**
 * Actual composited refraction/blur through the recorded background layer.
 *
 * Unlike the original Android backend, this uses Gaze Glassy's portable Skiko
 * runtime shader renderer. This is not a flat translucent color substitute.
 * The measured reference profile is shared with the Android implementation;
 * GPU shader parity still requires pixel-difference tests on each platform.
 */
fun Modifier.glasmioLiquidGlass(
    backdrop: Backdrop,
    material: NavigationGlassMaterial = NavigationGlassMaterial.RegularLarge,
    sampleMinimumDimension: Dp = 150.dp,
    shape: Shape? = null,
): Modifier = composed {
    val colors = LocalGlassColors.current
    val appearance = LocalGlassAppearance.current
    val density = LocalDensity.current
    val surfaceShape = shape ?: LocalGlasmioShapes.current.medium
    val geometry = navigationGlassGeometryProfile(
        minimumDimension = sampleMinimumDimension.value,
        material = material,
    )
    if (!appearance.usesBackdrop) {
        this.clip(surfaceShape).background(colors.elevatedBackground)
    } else {
        val fills = material.fills(colors.isDark)
        this.drawBackdrop(
            backdrop = backdrop,
            shape = { surfaceShape },
            effects = {
                // Blur inside the sampled layer, then refract its output; the edge
                // remains defined by the same G2 shape on every target.
                blur(radius = geometry.blurRadius * density.density)
                if (appearance.usesRefraction) {
                    lens(
                        refractionHeight = geometry.innerHeight * density.density,
                        refractionAmount = geometry.innerAmount * density.density,
                        chromaticAberration = true,
                    )
                }
            },
            onDrawSurface = {
                fills.forEach { fill ->
                    drawRect(fill.color, blendMode = fill.blendMode)
                }
            },
        )
    }
}
