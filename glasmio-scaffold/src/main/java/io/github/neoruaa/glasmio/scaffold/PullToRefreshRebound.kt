package io.github.neoruaa.glasmio.scaffold

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.LocalGlassColors
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI

private val RefreshReboundSpring = spring<Float>(
    dampingRatio = 1f,
    stiffness = (4.0 * PI * PI / (0.4 * 0.4)).toFloat(),
    visibilityThreshold = 0.1f,
)
private val GlasmioRefreshThreshold = 80.dp

class GlasmioPullToRefreshState {
    var distanceFraction by mutableFloatStateOf(0f)
        internal set
    var isAnimating by mutableStateOf(false)
        internal set
}

@Composable
fun rememberGlasmioPullToRefreshState(): GlasmioPullToRefreshState =
    remember { GlasmioPullToRefreshState() }

/**
 * Pull-to-refresh nested scroll, driven by Glasmio state.
 * Parent consumes only unused downward scrolling; list scrolling remains native.
 */
@Composable
fun Modifier.glasmioPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    state: GlasmioPullToRefreshState,
    enabled: Boolean = true,
): Modifier {
    val threshold = with(LocalDensity.current) { GlasmioRefreshThreshold.toPx() }
    val refreshAction by rememberUpdatedState(onRefresh)
    val refreshingNow by rememberUpdatedState(isRefreshing)
    val enabledNow by rememberUpdatedState(enabled)
    val connection = remember(state, threshold) {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (!enabledNow || refreshingNow || source != NestedScrollSource.UserInput) return Offset.Zero
                val delta = available.y
                if (delta > 0f || (delta < 0f && state.distanceFraction > 0f)) {
                    val before = state.distanceFraction
                    state.distanceFraction = (before + delta / (2f * threshold)).coerceAtLeast(0f)
                    val used = (state.distanceFraction - before) * 2f * threshold
                    return Offset(0f, used)
                }
                return Offset.Zero
            }
            override suspend fun onPreFling(available: Velocity): Velocity {
                if (!enabledNow || state.distanceFraction == 0f) return Velocity.Zero
                val triggered = state.distanceFraction >= 1f && !refreshingNow
                if (triggered) refreshAction()
                state.isAnimating = true
                try {
                    animate(state.distanceFraction, 0f, animationSpec = RefreshReboundSpring) { value, _ ->
                        state.distanceFraction = value
                    }
                } finally {
                    state.distanceFraction = 0f
                    state.isAnimating = false
                }
                return Velocity.Zero
            }
        }
    }
    return this.nestedScroll(connection)
}

/** Platform-independent, non-Material refresh indicator. */
@Composable
fun GlasmioPullToRefreshIndicator(
    isRefreshing: Boolean,
    state: GlasmioPullToRefreshState,
    modifier: Modifier = Modifier,
) {
    val tint = LocalGlassColors.current.accent
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            while (true) {
                rotation.animateTo(360f, androidx.compose.animation.core.tween(900))
                rotation.snapTo(0f)
            }
        } else rotation.snapTo(0f)
    }
    val fraction = if (isRefreshing) 1f else state.distanceFraction.coerceIn(0f, 1f)
    if (fraction <= 0f) return
    Canvas(modifier.size(28.dp).graphicsLayer { alpha = fraction }) {
        val width = 2.5.dp.toPx()
        drawArc(
            color = tint,
            startAngle = -90f + rotation.value,
            sweepAngle = (280f * fraction).coerceAtLeast(5f),
            useCenter = false,
            style = Stroke(width = width, cap = StrokeCap.Round),
        )
    }
}

/** Follows finger displacement, then rebounds regardless of refresh activity. */
@Composable
fun Modifier.pullToRefreshRebound(
    state: GlasmioPullToRefreshState,
    isRefreshing: Boolean,
    enabled: Boolean = true,
): Modifier {
    val threshold = with(LocalDensity.current) { GlasmioRefreshThreshold.toPx() }
    val rebound = remember(state) { Animatable(0f) }
    var followingPull by remember(state) { mutableStateOf(false) }
    val refreshingNow by rememberUpdatedState(isRefreshing)
    val enabledNow by rememberUpdatedState(enabled)

    LaunchedEffect(state, threshold) {
        var canFollowPull = true
        var lastPullOffset = 0f
        var returnAnimation: Job? = null
        snapshotFlow { Triple(state.distanceFraction, state.isAnimating || refreshingNow, enabledNow) }
            .collect { (fraction, released, available) ->
                if (!available) {
                    returnAnimation?.cancel()
                    rebound.snapTo(0f)
                    followingPull = false
                    canFollowPull = false
                    lastPullOffset = 0f
                } else if (released) {
                    canFollowPull = false
                    if (followingPull) {
                        val offset = maxOf(lastPullOffset, rebound.value)
                        returnAnimation?.cancel()
                        rebound.snapTo(offset)
                        followingPull = false
                        returnAnimation = launch { rebound.animateTo(0f, RefreshReboundSpring) }
                    }
                } else if (fraction <= 0f) {
                    canFollowPull = true
                    followingPull = false
                    lastPullOffset = 0f
                } else if (canFollowPull) {
                    lastPullOffset = fraction * threshold
                    followingPull = true
                }
            }
    }
    return this.clipToBounds().graphicsLayer {
        translationY = when {
            !enabled -> 0f
            followingPull -> maxOf(state.distanceFraction.coerceAtLeast(0f) * threshold, rebound.value)
            else -> rebound.value
        }
    }
}
