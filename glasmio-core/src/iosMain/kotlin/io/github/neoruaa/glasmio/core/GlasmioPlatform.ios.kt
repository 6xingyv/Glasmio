package io.github.neoruaa.glasmio.core

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/** UIKit manages its own safe-area insets; there is no Android View inset bug on iOS. */
@Composable
actual fun ProvideGlasmioDisplayCutoutFix() = Unit

/** TODO: replace with a faithful continuous Superellipse path on iOS. */
internal actual fun glasmioContinuousRoundedRectangle(radius: Dp): CornerBasedShape =
    RoundedCornerShape(radius)
