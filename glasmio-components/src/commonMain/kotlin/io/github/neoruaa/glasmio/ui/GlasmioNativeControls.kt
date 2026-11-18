package io.github.neoruaa.glasmio.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.core.IosTypography
import io.github.neoruaa.glasmio.core.LocalGlassColors
import io.github.neoruaa.glasmio.core.LocalGlasmioShapes

/** Minimal native Glasmio surface for all Compose targets, without a UI toolkit dependency. */
@Composable
fun GlasmioNativePanel(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalGlassColors.current
    val shape = LocalGlasmioShapes.current.medium
    Column(
        modifier.clip(shape).background(colors.elevatedBackground).padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
fun GlasmioNativeButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = LocalGlassColors.current
    val shape = LocalGlasmioShapes.current.small
    GlasmioText(
        label,
        modifier.clip(shape).background(
            colors.accent.copy(alpha = if (enabled) 1f else 0.35f),
        ).clickable(enabled = enabled, onClick = onClick)
            .defaultMinSize(minHeight = 44.dp)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        color = Color.White,
        style = IosTypography.headline,
    )
}

/** UIKit switch dimensions and spring-driven thumb positions, shared by Desktop and iOS. */
@Composable
fun GlasmioNativeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = LocalGlassColors.current
    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(dampingRatio = .82f, stiffness = 700f),
        label = "glasmio-switch",
    )
    Canvas(
        modifier.size(51.dp, 31.dp)
            .toggleable(checked, enabled = enabled, role = Role.Switch, onValueChange = onCheckedChange),
    ) {
        val radius = size.height / 2f
        drawRoundRect(
            color = if (checked) Color(0xFF34C759) else colors.systemGray4,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius),
        )
        val thumbRadius = 13.5.dp.toPx()
        val padding = 2.dp.toPx()
        val center = Offset(
            padding + thumbRadius + progress * (size.width - 2 * (padding + thumbRadius)),
            size.height / 2f,
        )
        drawCircle(Color.White, thumbRadius, center)
    }
}
