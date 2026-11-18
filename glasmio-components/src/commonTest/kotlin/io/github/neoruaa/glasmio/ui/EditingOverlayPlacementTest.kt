package io.github.neoruaa.glasmio.ui

import androidx.compose.ui.geometry.Rect
import kotlin.test.Test
import kotlin.test.assertEquals

class EditingOverlayPlacementTest {
    private val viewport = Rect(0f, 0f, 300f, 600f)

    @Test fun prefersAboveWhenSpaceIsAvailable() {
        val anchor = Rect(100f, 300f, 180f, 350f)
        assertEquals(190f, editingOverlayTop(anchor, viewport, 100f, 10f, preferAbove = true))
        assertEquals(360f, editingOverlayTop(anchor, viewport, 100f, 10f, preferAbove = false))
    }

    @Test fun fallsBackAndClampsToViewport() {
        val anchor = Rect(100f, 10f, 180f, 40f)
        assertEquals(50f, editingOverlayTop(anchor, viewport, 120f, 10f, preferAbove = true))
    }
}
