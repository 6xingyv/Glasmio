package io.github.neoruaa.glasmio.glass.render.optics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GlassOpticsTest {
    @Test fun displacementMatchesShaderAtBoundaryAndHalfDepth() {
        assertEquals(14f, GlassOptics.displacement(0f, 0.1f, 14f), 0.00001f)
        assertEquals(0f, GlassOptics.displacement(-20f, 0.1f, 14f), 0.00001f)
        assertEquals(14f * (1f - kotlin.math.sqrt(0.75f)),
            GlassOptics.displacement(-5f, 0.1f, 14f), 0.00001f)
        assertEquals(-14f, GlassOptics.displacement(0f, 0.1f, -14f), 0.00001f)
    }
    @Test fun refractionWeightHandlesRampsAndSteps() {
        assertEquals(0f, GlassOptics.refractionWeight(-5f, 0f, 10f, .8f))
        assertEquals(.4f, GlassOptics.refractionWeight(5f, 0f, 10f, .8f), 0.00001f)
        assertEquals(.8f, GlassOptics.refractionWeight(10f, 0f, 10f, .8f), 0.00001f)
        assertEquals(0f, GlassOptics.refractionWeight(9f, 10f, 10f, .8f))
    }
    @Test fun invalidArgumentsAreRejected() {
        assertFailsWith<IllegalArgumentException> { GlassOptics.displacement(0f, 0f, 1f) }
        assertFailsWith<IllegalArgumentException> { GlassOptics.refractionWeight(Float.NaN, 0f, 1f, 1f) }
    }
}
