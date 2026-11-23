package io.github.neoruaa.glasmio.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import io.github.neoruaa.glasmio.core.GlasmioBuiltInSymbols
import io.github.neoruaa.glasmio.core.GlasmioText
import io.github.neoruaa.glasmio.icons.SfSymbolCatalog

/**
 * Optional font supplied by the Desktop application, never bundled by Glasmio.
 * Use a font containing the private SF Symbols codepoints when the application
 * has rights to use it. Without the font, Android and Desktop use the exact same
 * GlasmioBuiltInSymbols vector fallbacks.
 */
val LocalGlasmioDesktopSymbolFont = staticCompositionLocalOf<FontFamily?> { null }

@Composable
actual fun GlasmioSymbolIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
) {
    val symbolFont = LocalGlasmioDesktopSymbolFont.current
    val codePoint = if (symbolFont != null) SfSymbolCatalog.codePointOrNull(systemName) else null
    if (symbolFont != null && codePoint != null) {
        val semanticsModifier = if (contentDescription == null) {
            Modifier.clearAndSetSemantics {}
        } else {
            Modifier.semantics { this.contentDescription = contentDescription }
        }
        Box(modifier.size(size).then(semanticsModifier), contentAlignment = Alignment.Center) {
            GlasmioText(
                text = String(Character.toChars(codePoint)),
                color = tint,
                fontFamily = symbolFont,
                fontSize = size.value.sp,
                letterSpacing = 0.sp,
                maxLines = 1,
                softWrap = false,
            )
        }
    } else {
        Image(
            imageVector = GlasmioBuiltInSymbols.resolve(systemName),
            contentDescription = contentDescription,
            modifier = modifier.size(size),
            colorFilter = ColorFilter.tint(tint),
        )
    }
}
