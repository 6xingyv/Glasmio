package io.github.neoruaa.glasmio.core

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.dp

/** Glasmio's own semantic shapes; no Material theme or component dependency. */
@Immutable
data class GlasmioShapes(
    val extraSmall: CornerBasedShape = glasmioContinuousRoundedRectangle(8.dp),
    val small: CornerBasedShape = glasmioContinuousRoundedRectangle(16.dp),
    val medium: CornerBasedShape = glasmioContinuousRoundedRectangle(26.dp),
    val large: CornerBasedShape = glasmioContinuousRoundedRectangle(38.dp),
    val extraLarge: CornerBasedShape = glasmioContinuousRoundedRectangle(44.dp),
)

@Immutable
data class GlasmioSpacing(
    val compact: Dp = 8.dp,
    val regular: Dp = 16.dp,
    val section: Dp = 32.dp,
)

object GlasmioMotion {
    fun <T> spring() = spring<T>(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow)
}

val LocalGlasmioShapes = staticCompositionLocalOf { GlasmioShapes() }
val LocalGlasmioSpacing = staticCompositionLocalOf { GlasmioSpacing() }

/** Apple typography tables are the source of truth on every platform. */
fun glasmioTypography(fontFamily: FontFamily = FontFamily.Default): GlasmioTextStyles =
    glasmioTextStyles(fontFamily)

/** Glasmio semantic palette; no Material ColorScheme adapter is involved. */
fun glasmioColorScheme(isDark: Boolean, accent: Color = if (isDark) Color(0xFF0091FF) else Color(0xFF0088FF)): GlassColors =
    defaultGlassColors(isDark, accent)

@Composable
fun GlasmioTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    typography: GlasmioTextStyles = glasmioTypography(),
    colors: GlassColors = glasmioColorScheme(isDark),
    appearance: GlassAppearance = GlassAppearance(),
    dimensions: GlassDimensions = GlassDimensions(),
    shapes: GlasmioShapes = GlasmioShapes(),
    spacing: GlasmioSpacing = GlasmioSpacing(),
    dynamicTypeSize: IosDynamicTypeSize? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    content: @Composable () -> Unit,
) {
    ProvideGlasmioDisplayCutoutFix()
    val textStyles = rememberGlasmioTextStyles(dynamicTypeSize, typography, letterSpacing)
    CompositionLocalProvider(
        LocalGlassColors provides colors,
        LocalGlassAppearance provides appearance,
        LocalGlassDimensions provides dimensions,
        LocalGlasmioShapes provides shapes,
        LocalGlasmioSpacing provides spacing,
        LocalGlasmioTextStyles provides textStyles,
        LocalGlasmioLetterSpacing provides letterSpacing,
        LocalGlasmioTextStyle provides textStyles.body,
        LocalGlasmioContentColor provides colors.content,
        content = content,
    )
}

object GlasmioTheme {
    @Composable @ReadOnlyComposable
    fun letterSpacing(fallback: TextUnit): TextUnit = LocalGlasmioLetterSpacing.current.let {
        if (it.isUnspecified) fallback else it
    }
    val colors: GlassColors @Composable @ReadOnlyComposable get() = LocalGlassColors.current
    val appearance: GlassAppearance @Composable @ReadOnlyComposable get() = LocalGlassAppearance.current
    val dimensions: GlassDimensions @Composable @ReadOnlyComposable get() = LocalGlassDimensions.current
    val shapes: GlasmioShapes @Composable @ReadOnlyComposable get() = LocalGlasmioShapes.current
    val spacing: GlasmioSpacing @Composable @ReadOnlyComposable get() = LocalGlasmioSpacing.current
    val typography: GlasmioTextStyles @Composable @ReadOnlyComposable get() = LocalGlasmioTextStyles.current
    val textStyles: GlasmioTextStyles @Composable @ReadOnlyComposable get() = LocalGlasmioTextStyles.current
}
