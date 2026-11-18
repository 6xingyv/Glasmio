package io.github.neoruaa.glasmio.core

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle

/** Android's OEM inset workaround is the only platform-specific core UI operation. */
@Composable
expect fun ProvideGlasmioDisplayCutoutFix()

/** The exact same G2 continuous-corner geometry on Android, Desktop and iOS. */
internal fun glasmioContinuousRoundedRectangle(radius: Dp): CornerBasedShape =
    ContinuousRoundedRectangle(radius)
