package io.github.neoruaa.glasmio.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoupeSharedMotionTest {
    @Test
    fun responseMapsToNativeSpringStiffness() {
        assertEquals(987f, loupeSpringStiffness(.2f), .5f)
        assertEquals(158f, loupeSpringStiffness(.5f), .5f)
    }

    @Test
    fun smoothingDoesNotDependOnRefreshRate() {
        val frame = 1f / 60f
        val a = loupeLowPassAlpha(.3f, frame)
        assertEquals(.3f, a, .00001f)
        val half = loupeLowPassAlpha(.3f, frame * .5f)
        assertEquals(a, 1f - (1f - half) * (1f - half), .00001f)
    }

    @Test
    fun lensCenterRespectsHorizontalViewportBounds() {
        val x = loupeClampX(-40f, 0f, 400f, 115f, 8f)
        assertEquals(65.5f, x, .00001f)
        assertTrue(loupeLineBreakProgress(LoupeLineBreakDistance * 4f) >= LoupeDismissalThreshold)
    }
}
