package io.github.neoruaa.glasmio.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentModeScaleAspectFit

/**
 * UIKit draws the native platform symbol, preserving iOS optical alignment and
 * adaptive system glyph metrics instead of loading a private Apple font.
 */
@Composable
actual fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
) {
    UIKitView(
        factory = {
            UIImageView().apply {
                contentMode = UIViewContentModeScaleAspectFit
            }
        },
        modifier = modifier.size(size),
        update = { view ->
            view.image = UIImage.systemImageNamed(systemName)
                ?: UIImage.systemImageNamed("questionmark.square.dashed")
            view.tintColor = UIColor.colorWithRed(
                red = tint.red.toDouble(),
                green = tint.green.toDouble(),
                blue = tint.blue.toDouble(),
                alpha = tint.alpha.toDouble(),
            )
            view.isAccessibilityElement = contentDescription != null
            view.accessibilityLabel = contentDescription
        },
    )
}
