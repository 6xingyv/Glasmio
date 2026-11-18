package io.github.neoruaa.glasmio.core

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.unit.Dp
import com.kyant.capsule.ContinuousRoundedRectangle

internal actual fun glasmioContinuousRoundedRectangle(radius: Dp): CornerBasedShape =
    ContinuousRoundedRectangle(radius)
