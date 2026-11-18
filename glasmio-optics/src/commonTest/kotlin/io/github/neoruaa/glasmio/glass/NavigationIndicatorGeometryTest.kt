package io.github.neoruaa.glasmio.glass

import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationIndicatorGeometryTest {
    @Test fun deformationPreservesRestingDimensions() {
        val size = Size(100f, 28f)
        val deformed = segmentedIndicatorSize(
            restingSize = size, scaleX = 1f, scaleY = 1f, velocity = 0f, expansion = 16f,
        )
        assertEquals(size, deformed)
        val pressed = segmentedIndicatorSize(
            restingSize = size, scaleX = SegmentedIndicatorPressedScale,
            scaleY = 1f, velocity = 0f, expansion = 16f,
        )
        assertEquals(116f, pressed.width)
        assertEquals(28f, pressed.height)
    }

    @Test fun navigationLensAnchorsOnlySelectedPanel() {
        assertEquals(1.2f, navigationTabPanelScale(0, 0f, 1.2f))
        assertEquals(1f, navigationTabPanelScale(1, 0f, 1.2f))
        assertTrue(navigationLensSourceHeight(20f, 100f) >= 1f)
    }
}
