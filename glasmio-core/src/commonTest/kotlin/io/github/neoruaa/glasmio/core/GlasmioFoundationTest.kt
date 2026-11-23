package io.github.neoruaa.glasmio.core

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GlasmioFoundationTest {
    @Test
    fun semanticTypographyUsesNativeMetrics() {
        val styles = glasmioTypography()
        assertEquals(17.sp, styles.body.fontSize)
        assertEquals(22.sp, styles.body.lineHeight)
        assertEquals(34.sp, styles.largeTitle.fontSize)
        assertEquals(0.5.sp, styles.withLetterSpacing(0.5.sp).body.letterSpacing)
    }

    @Test
    fun nativeG2ShapeIsSharedAndNotCircularRoundedCornerShape() {
        val shape = GlasmioShapes().medium
        val outline = shape.createOutline(
            size = Size(180f, 100f),
            layoutDirection = LayoutDirection.Ltr,
            density = Density(1f),
        )
        assertTrue(outline is Outline.Generic)
    }

    @Test
    fun androidAndDesktopFallbackSymbolsShareOneVectorCatalog() {
        assertEquals(
            GlasmioBuiltInSymbols.resolve("house.fill"),
            GlasmioBuiltInSymbols.resolve("house"),
        )
        assertEquals(
            GlasmioBuiltInSymbols.resolve("magnifyingglass"),
            GlasmioBuiltInSymbols.resolve("magnifyingglass.circle"),
        )
    }
}
