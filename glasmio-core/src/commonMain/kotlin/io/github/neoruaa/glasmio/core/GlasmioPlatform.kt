package io.github.neoruaa.glasmio.core

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/** Android repairs OEM cutout dispatch; other targets have no such View workaround. */
@Composable
expect fun ProvideGlasmioDisplayCutoutFix()

/** Android uses Capsule's continuous curvature; desktop uses a Compose approximation. */
internal expect fun glasmioContinuousRoundedRectangle(radius: Dp): CornerBasedShape
