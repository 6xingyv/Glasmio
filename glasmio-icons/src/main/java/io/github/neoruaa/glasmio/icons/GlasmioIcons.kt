package io.github.neoruaa.glasmio.icons

import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.neoruaa.glasmio.core.GlasmioBuiltInSymbols
import io.github.neoruaa.glasmio.core.LocalGlasmioContentColor

fun interface GlasmioIconResolver {
    fun resolve(systemName: String): ImageVector?
}

/** Identical non-Material vector fallbacks for Android and Desktop. */
object GlasmioVectorIconResolver : GlasmioIconResolver {
    override fun resolve(systemName: String): ImageVector = GlasmioBuiltInSymbols.resolve(systemName)
}

/** Host-owned SF font; never bundled or redistributed by Glasmio. */
val LocalGlasmioSymbolTypeface = staticCompositionLocalOf<Typeface?> { null }
val LocalGlasmioIconResolver = staticCompositionLocalOf<GlasmioIconResolver> { GlasmioVectorIconResolver }
val LocalGlasmioIconTint = staticCompositionLocalOf<@Composable () -> Color> { { LocalGlasmioContentColor.current } }

@Composable
fun GlasmioIconProvider(
    resolver: GlasmioIconResolver = GlasmioVectorIconResolver,
    symbolTypeface: Typeface? = null,
    defaultTint: @Composable () -> Color = { LocalGlasmioContentColor.current },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalGlasmioIconResolver provides resolver,
        LocalGlasmioSymbolTypeface provides symbolTypeface,
        LocalGlasmioIconTint provides defaultTint,
        content = content,
    )
}

@Composable
internal fun GlasmioResolvedIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier,
    tint: Color,
    size: Dp,
    mirrored: Boolean,
) {
    val vector = LocalGlasmioIconResolver.current.resolve(systemName)
        ?: GlasmioBuiltInSymbols.resolve(systemName)
    Image(
        imageVector = vector,
        contentDescription = contentDescription,
        modifier = modifier.size(size).then(
            if (mirrored && !vector.autoMirror) Modifier.scale(-1f, 1f) else Modifier
        ),
        colorFilter = ColorFilter.tint(tint),
    )
}

@Composable
fun GlasmioIcon(
    systemName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalGlasmioIconTint.current(),
    size: Dp = 24.dp,
) = SfIcon(systemName, contentDescription, modifier, tint, size)
