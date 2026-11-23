package io.github.neoruaa.glasmio.backdrop

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class OpticalOutlineTest {
    @Test
    fun preserveRealG2OutlineWithoutRoundedRectangleShortcut() {
        val g2 = ContinuousRoundedRectangle(26.dp)
        val shaderShape = signedDistanceShape(g2)
        assertFalse(shaderShape is CornerBasedShape)
        val size = Size(160f, 76f)
        val actual = shaderShape.createOutline(size, LayoutDirection.Ltr, Density(2f))
        val reference = g2.createOutline(size, LayoutDirection.Ltr, Density(2f))
        assertEquals(reference, actual)
        assertTrue(actual is Outline.Generic)
    }

    @Test
    fun ordinaryCornerShapesKeepFastPath() {
        val regular = RoundedCornerShape(12.dp)
        assertSame(regular, signedDistanceShape(regular))
    }
}
