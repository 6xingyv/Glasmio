package io.github.neoruaa.glasmio.glass

import androidx.compose.ui.geometry.Rect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LiquidMenuMorphMotionKmpTest {
    @Test fun motionInitializesAndProducesFiniteFrames() {
        val motion = LiquidMenuMorphMotion()
        val source = Rect(20f, 20f, 80f, 60f)
        val destination = Rect(10f, 70f, 250f, 350f)
        motion.initialize(source, destination, 24f)
        assertTrue(motion.initialized)
        assertEquals(source, motion.sourceBounds)
        assertTrue(motion.frame.bounds.width.isFinite())
        assertTrue(motion.frame.bounds.height.isFinite())
        motion.retarget(true)
        repeat(60) { motion.advanceBy(1f / 60f) }
        assertTrue(motion.frame.contentProgress in 0f..1f)
        assertTrue(motion.frame.lensProgress in 0f..1f)
        assertTrue(motion.frame.bounds.width > 0f)
        assertTrue(motion.frame.bounds.height > 0f)
    }

    @Test fun analyticalSpringSettlesRegardlessOfFrameRate() {
        val slow = LiquidMenuSpring()
        val fast = LiquidMenuSpring()
        val spec = LiquidMenuSpringSpec(.8f, .3f)
        for (spring in listOf(slow, fast)) {
            spring.reset(0f)
            spring.target(100f, spec)
        }
        repeat(60) { slow.advance(1f / 60f) }
        repeat(30) { fast.advance(1f / 30f) }
        assertEquals(slow.value, fast.value, 0.2f)
    }
}
