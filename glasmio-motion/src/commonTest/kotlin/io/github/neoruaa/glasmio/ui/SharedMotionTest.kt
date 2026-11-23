package io.github.neoruaa.glasmio.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedMotionTest {
    @Test fun messageContextFramesReachFinalValues() {
        assertEquals(1f, MessageContextMotion.value(MessageContextMotion.Channel.Content, 1_250_000_000L, true))
        assertEquals(0f, MessageContextMotion.value(MessageContextMotion.Channel.Content, 900_000_000L, false))
    }
    @Test fun alertSpringIsBoundedAtEndpoints() {
        assertEquals(0f, IosAlertMotion.Timing.transform(0f))
        assertEquals(1f, IosAlertMotion.Timing.transform(1f))
        assertTrue(IosAlertMotion.Timing.transform(.5f) in 0f..1f)
    }
}
