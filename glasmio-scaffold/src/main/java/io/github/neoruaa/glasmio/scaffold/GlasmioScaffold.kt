package io.github.neoruaa.glasmio.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.LocalGlassColors

private enum class GlasmioScaffoldSlot { TopBar, BottomBar, Content }

/**
 * Glasmio scaffold: bars overlay the window, and body padding accounts for their
 * measured height and safe drawing insets. No Material measuring, theming or slots.
 */
@Composable
fun GlasmioScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    contentWindowInsets: WindowInsets = WindowInsets.safeDrawing,
    containerColor: Color = LocalGlassColors.current.groupedBackground,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout(modifier.background(containerColor)) { constraints ->
        val barConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val top = subcompose(GlasmioScaffoldSlot.TopBar, topBar).map { it.measure(barConstraints) }
        val bottom = subcompose(GlasmioScaffoldSlot.BottomBar, bottomBar).map { it.measure(barConstraints) }
        val topHeight = top.maxOfOrNull { it.height } ?: 0
        val bottomHeight = bottom.maxOfOrNull { it.height } ?: 0
        val leftInset = contentWindowInsets.getLeft(this, layoutDirection)
        val rightInset = contentWindowInsets.getRight(this, layoutDirection)
        val topPadding = maxOf(topHeight, contentWindowInsets.getTop(this))
        val bottomPadding = maxOf(bottomHeight, contentWindowInsets.getBottom(this))
        val padding = PaddingValues(
            start = with(this) { leftInset.toDp() },
            top = with(this) { topPadding.toDp() },
            end = with(this) { rightInset.toDp() },
            bottom = with(this) { bottomPadding.toDp() },
        )
        val body = subcompose(GlasmioScaffoldSlot.Content) { content(padding) }
            .map { it.measure(constraints) }
        layout(constraints.maxWidth, constraints.maxHeight) {
            body.forEach { it.placeRelative(0, 0) }
            top.forEach { it.placeRelative(0, 0) }
            bottom.forEach { it.placeRelative(0, constraints.maxHeight - it.height) }
        }
    }
}

@Composable
fun GlasmioListPage(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    GlasmioOverscrollHost(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().consumeWindowInsets(contentPadding),
            state = state,
            contentPadding = contentPadding,
            content = content,
        )
    }
}
