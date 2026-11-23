package io.github.neoruaa.glasmio.backdrop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mocharealm.gaze.glassy.liquid.effect.Backdrop
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlassColors
import io.github.neoruaa.glasmio.core.LocalGlasmioShapes
import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial

/**
 * Shared GPU glass composable for Android, Desktop and iOS. One source set owns
 * its outline, optical recipe and content padding on every supported target.
 * This uses the common Skiko pipeline; native Android custom AGSL controls may
 * continue to use their specialized implementation.
 */
@Composable
fun GlasmioGlassPanel(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    material: NavigationGlassMaterial = NavigationGlassMaterial.RegularLarge,
    shape: Shape = LocalGlasmioShapes.current.medium,
    sampleMinimumDimension: Dp = 150.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .glasmioLiquidGlass(
                backdrop = backdrop,
                material = material,
                shape = shape,
                sampleMinimumDimension = sampleMinimumDimension,
            )
            .padding(contentPadding),
        content = content,
    )
}

/** Non-Material glass button with an accessible Compose Foundation click target. */
@Composable
fun GlasmioGlassButton(
    backdrop: Backdrop,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    material: NavigationGlassMaterial = NavigationGlassMaterial.Clear,
) {
    val shape = LocalGlasmioShapes.current.small
    val colors = LocalGlassColors.current
    Box(
        modifier
            .defaultMinSize(minHeight = 44.dp)
            .glasmioLiquidGlass(
                backdrop = backdrop,
                material = material,
                shape = shape,
                sampleMinimumDimension = 44.dp,
            )
            .clip(shape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
    ) {
        GlasmioText(
            text = label,
            color = if (enabled) colors.content else colors.tertiaryContent,
            style = IosTypography.headline,
            maxLines = 1,
        )
    }
}
