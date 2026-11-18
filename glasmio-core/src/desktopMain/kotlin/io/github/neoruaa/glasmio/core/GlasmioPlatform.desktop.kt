package io.github.neoruaa.glasmio.core

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
actual fun ProvideGlasmioDisplayCutoutFix() = Unit

internal actual fun glasmioContinuousRoundedRectangle(radius: Dp): CornerBasedShape =
    RoundedCornerShape(radius)
