package io.github.neoruaa.glasmio.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * License-independent original line pictograms used by the entire library as a
 * fallback when an actual system symbol font or platform glyph is unavailable.
 * The same vectors render on Android and Desktop; no Material icons are linked.
 *
 * Apps with an SF Symbols font can retain the native glyphs through SfIcon's provider.
 */
object GlasmioBuiltInSymbols {
    private val pathData = mapOf(
        "house" to "M3 10 L12 3 L21 10 V21 H14 V14 H10 V21 H3 Z",
        "gear" to "M12 3 V5 M12 19 V21 M3 12 H5 M19 12 H21 M5.6 5.6 L7 7 M17 17 L18.4 18.4 M18.4 5.6 L17 7 M7 17 L5.6 18.4 M16.5 12 A4.5 4.5 0 1 1 7.5 12 A4.5 4.5 0 1 1 16.5 12 Z",
        "search" to "M11 3 A8 8 0 1 1 3 11 A8 8 0 1 1 11 3 Z M17 17 L22 22",
        "plus" to "M12 4 V20 M4 12 H20",
        "minus" to "M4 12 H20",
        "close" to "M5 5 L19 19 M19 5 L5 19",
        "check" to "M4 12 L10 18 L20 6",
        "heart" to "M12 21 L4.5 13.5 C-1 8 4 2 9 5 C11 6 12 8 12 8 C12 8 13 6 15 5 C20 2 25 8 19.5 13.5 Z",
        "star" to "M12 2 L15 9 L22 9.5 L16.5 14.5 L18 22 L12 18 L6 22 L7.5 14.5 L2 9.5 L9 9 Z",
        "back" to "M14 5 L7 12 L14 19",
        "forward" to "M10 5 L17 12 L10 19",
        "up" to "M5 14 L12 7 L19 14",
        "down" to "M5 10 L12 17 L19 10",
        "arrowBack" to "M20 12 H4 M11 5 L4 12 L11 19",
        "arrowForward" to "M4 12 H20 M13 5 L20 12 L13 19",
        "play" to "M7 4 L20 12 L7 20 Z",
        "pause" to "M7 4 V20 M17 4 V20",
        "music" to "M9 17 V5 L20 3 V15 M9 5 L20 3 M9 17 A3 2.5 0 1 1 3 17 A3 2.5 0 1 1 9 17 Z M20 15 A3 2.5 0 1 1 14 15 A3 2.5 0 1 1 20 15 Z",
        "person" to "M12 11 A4 4 0 1 1 12 3 A4 4 0 1 1 12 11 Z M4 21 V19 C4 11 20 11 20 19 V21",
        "clock" to "M12 3 A9 9 0 1 1 12 21 A9 9 0 1 1 12 3 Z M12 7 V12 L16 14",
        "more" to "M5 12 L5.01 12 M12 12 L12.01 12 M19 12 L19.01 12",
        "warning" to "M12 3 L22 21 H2 Z M12 9 V15 M12 18 L12 18",
        "trash" to "M4 6 H20 M9 6 V4 H15 V6 M6 6 L7 21 H17 L18 6 M10 10 V17 M14 10 V17",
        "edit" to "M4 20 L9 19 L20 8 L16 4 L5 15 Z M14 6 L18 10",
        "bell" to "M5 18 H19 L17 15 V10 C17 4 7 4 7 10 V15 Z M10 21 H14",
        "calendar" to "M3 6 H21 V21 H3 Z M3 11 H21 M8 3 V8 M16 3 V8",
        "image" to "M3 4 H21 V20 H3 Z M6 17 L11 12 L14 15 L17 11 L21 16 M8 8 L8.01 8",
        "cloud" to "M6 19 C1 19 1 12 6 11 C7 2 18 2 19 11 C25 13 22 19 18 19 Z",
        "mic" to "M9 4 C9 1 15 1 15 4 V13 C15 17 9 17 9 13 Z M5 11 C5 23 19 23 19 11 M12 19 V23",
        "wifi" to "M2 9 Q12 0 22 9 M5 13 Q12 7 19 13 M9 17 Q12 14 15 17 M12 21 L12 21",
        "refresh" to "M20 9 A8 8 0 1 0 20 15 M20 3 V9 H14",
        "info" to "M12 3 A9 9 0 1 1 12 21 A9 9 0 1 1 12 3 Z M12 11 V17 M12 7 L12 7",
        "question" to "M12 3 A9 9 0 1 1 12 21 A9 9 0 1 1 12 3 Z M9 9 A3 3 0 0 1 15 9 C15 11 12 11 12 14 M12 17 L12 17",
        "menu" to "M4 6 H20 M4 12 H20 M4 18 H20",
        "list" to "M4 6 H4.01 M8 6 H20 M4 12 H4.01 M8 12 H20 M4 18 H4.01 M8 18 H20",
        "square" to "M4 4 H20 V20 H4 Z",
    )

    private val vectors by lazy {
        pathData.mapValues { (name, path) ->
            ImageVector.Builder(
                name = "Glasmio-$name",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                addPath(
                    pathData = PathParser().parsePathString(path).toNodes(),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 1.8f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                )
            }.build()
        }
    }

    fun resolve(systemName: String): ImageVector {
        val key = when {
            systemName.startsWith("house") -> "house"
            systemName.startsWith("gear") || systemName.startsWith("slider") -> "gear"
            systemName.startsWith("magnifyingglass") -> "search"
            systemName.startsWith("checkmark") -> "check"
            systemName.startsWith("plus") -> "plus"
            systemName.startsWith("minus") -> "minus"
            systemName.startsWith("xmark") -> "close"
            systemName.startsWith("heart") -> "heart"
            systemName.startsWith("star") -> "star"
            systemName.startsWith("chevron.left") || systemName.startsWith("chevron.backward") -> "back"
            systemName.startsWith("chevron.right") || systemName.startsWith("chevron.forward") -> "forward"
            systemName.startsWith("chevron.up") -> "up"
            systemName.startsWith("chevron.down") -> "down"
            systemName.startsWith("arrow.backward") || systemName.startsWith("arrow.left") -> "arrowBack"
            systemName.startsWith("arrow.forward") || systemName.startsWith("arrow.right") -> "arrowForward"
            systemName.startsWith("arrow.clockwise") -> "refresh"
            systemName.startsWith("play") -> "play"
            systemName.startsWith("pause") -> "pause"
            systemName.startsWith("music.note") || systemName.startsWith("waveform") -> "music"
            systemName.startsWith("person") -> "person"
            systemName.startsWith("clock") -> "clock"
            systemName.startsWith("ellipsis") -> "more"
            systemName.startsWith("exclamationmark") -> "warning"
            systemName.startsWith("trash") -> "trash"
            systemName.startsWith("pencil") -> "edit"
            systemName.startsWith("bell") -> "bell"
            systemName.startsWith("calendar") -> "calendar"
            systemName.startsWith("photo") -> "image"
            systemName.startsWith("icloud") || systemName.startsWith("cloud") -> "cloud"
            systemName.startsWith("mic") -> "mic"
            systemName.startsWith("wifi") -> "wifi"
            systemName.startsWith("info") -> "info"
            systemName.startsWith("questionmark") -> "question"
            systemName.startsWith("line.3.horizontal") -> "menu"
            systemName.startsWith("list.") -> "list"
            else -> "square"
        }
        return requireNotNull(vectors[key])
    }
}
