package io.github.neoruaa.glasmio.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MessageBubbleShapeTest {
    @Test
    fun incomingAndOutgoingShapesHaveValidMirroredContours() {
        for (outgoing in listOf(false, true)) {
            for (direction in listOf(LayoutDirection.Ltr, LayoutDirection.Rtl)) {
                val shape = MessageBubbleShape(outgoing = outgoing, showTail = true)
                val outline = assertIs<Outline.Generic>(
                    shape.createOutline(Size(200f, 90f), direction, Density(1f)),
                )
                val bounds = outline.path.getBounds()
                assertTrue(bounds.width > 0f && bounds.height > 0f)
                assertTrue(bounds.left >= -1f && bounds.top >= -1f)
                assertTrue(bounds.right <= 201f && bounds.bottom <= 91f)
            }
        }
    }

    @Test
    fun degenerateShapeProducesEmptyGenericOutline() {
        assertIs<Outline.Generic>(
            MessageBubbleShape(true).createOutline(Size.Zero, LayoutDirection.Ltr, Density(1f)),
        )
    }
}
