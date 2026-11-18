package io.github.neoruaa.glasmio.core

import androidx.compose.runtime.Composable

/** UIKit manages its own safe-area insets; there is no Android View inset bug on iOS. */
@Composable
actual fun ProvideGlasmioDisplayCutoutFix() = Unit
