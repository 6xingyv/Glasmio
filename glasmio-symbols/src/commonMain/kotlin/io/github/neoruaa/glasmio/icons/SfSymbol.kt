package io.github.neoruaa.glasmio.icons

import androidx.compose.runtime.Immutable

/** Code points are resolved by the SF Symbols CLI using exact-name matching. */
@Immutable
enum class SfSymbol(
    val systemName: String,
    val codePoint: Int,
    val autoMirrored: Boolean = false,
) {
    House("house", 0x10039E),
    HouseFilled("house.fill", 0x10039F),
    Layers("square.3.layers.3d", 0x100BF0),
    InfoCircle("info.circle", 0x100174),
    Sparkles("sparkles", 0x1001BF),
    MusicNote("music.note", 0x10046A),
    MusicNoteSquareStackFilled("music.note.square.stack.fill", 0x10343A),
    RadioWaves("dot.radiowaves.left.and.right", 0x100319),
    RadioFilled("radio.fill", 0x100A95),
    Safari("safari", 0x1003AC),
    SafariFilled("safari.fill", 0x1003AD),
    MusicNoteList("music.note.list", 0x10046C),
    Microphone("mic", 0x1002B0),
    Heart("heart", 0x1002B4),
    Star("star", 0x1002C2),
    StarFilled("star.fill", 0x1002C3),
    Download("arrow.down.circle", 0x100078),
    DownloadFilled("arrow.down.circle.fill", 0x100079),
    Cloud("icloud", 0x10030B),
    CloudFilled("icloud.fill", 0x10030C),
    Clock("clock", 0x10042B),
    ClockFilled("clock.fill", 0x10042C),
    ArrowClockwise("arrow.clockwise", 0x100148),
    Warning("exclamationmark.triangle", 0x1001FE),
    Search("magnifyingglass", 0x1002AB),
    Settings("gear", 0x10035F),
    Gearshape("gearshape", 0x1008CB),
    PersonFilled("person.fill", 0x10026A),
    Waveform("waveform", 0x10066B),
    PlayFilled("play.fill", 0x100284),
    PauseFilled("pause.fill", 0x100286),
    ForwardFilled("forward.fill", 0x10028C),
    BackwardFilled("backward.fill", 0x10028A),
    Ellipsis("ellipsis", 0x100360),
    Close("xmark", 0x100184),
    ChevronBack("chevron.left", 0x100189, autoMirrored = true),
}
