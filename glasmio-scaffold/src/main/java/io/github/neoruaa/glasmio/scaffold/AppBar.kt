package io.github.neoruaa.glasmio.scaffold

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/** Glasmio-owned header collapse state; independent of Material app bar components. */
class GlasmioAppBarState(
    initialHeightOffsetLimit: Float = 0f,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f,
) {
    var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)
    var heightOffset by mutableFloatStateOf(initialHeightOffset)
    var contentOffset by mutableFloatStateOf(initialContentOffset)
    val collapsedFraction: Float
        get() = if (heightOffsetLimit < 0f) (heightOffset / heightOffsetLimit).coerceIn(0f, 1f) else 0f
}

@Composable
fun rememberGlasmioAppBarState(): GlasmioAppBarState = remember { GlasmioAppBarState() }

@Composable
fun rememberGlasmioAppBarScrollBehavior(
    state: GlasmioAppBarState = rememberGlasmioAppBarState(),
    canScroll: () -> Boolean = { true },
    snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay(),
): GlasmioAppBarScrollBehavior = remember(state, snapAnimationSpec, flingAnimationSpec, canScroll) {
    GlasmioAppBarScrollBehavior(state, snapAnimationSpec, flingAnimationSpec, canScroll)
}

class GlasmioAppBarScrollBehavior(
    val state: GlasmioAppBarState,
    val snapAnimationSpec: AnimationSpec<Float>?,
    val flingAnimationSpec: DecayAnimationSpec<Float>?,
    val canScroll: () -> Boolean = { true },
) {
    val isPinned: Boolean = true
    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            if (!canScroll()) return Offset.Zero
            state.contentOffset += consumed.y
            state.heightOffset = (state.heightOffset + consumed.y)
                .coerceIn(state.heightOffsetLimit.coerceAtMost(0f), 0f)
            if (consumed.y == 0f && available.y > 0f &&
                (state.heightOffset == 0f || state.heightOffset == state.heightOffsetLimit)) {
                state.contentOffset = 0f
            }
            return Offset.Zero
        }
    }
}

suspend fun GlasmioAppBarState.resetHeightOffset() {
    if (heightOffset != 0f) {
        animate(initialValue = heightOffset, targetValue = 0f) { value, _ ->
            heightOffset = value
        }
    }
}
