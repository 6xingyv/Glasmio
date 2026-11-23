package io.github.neoruaa.glasmio.scaffold.navigation

import androidx.compose.runtime.Immutable

/**
 * Glasmio's native navigation presentation properties. Navigation3 provides
 * scene and metadata plumbing, not the visual design system.
 *
 * The former Miuix-specific NavDisplayTransitionEffects is intentionally no
 * longer used or required at compile time.
 */
@Immutable
data class GlasmioNavDisplayTransitionEffects(
    val enableCornerClip: Boolean = true,
    val dimAmount: Float = 0.1f,
    val blockInputDuringTransition: Boolean = true,
) {
    init { require(dimAmount in 0f..1f) { "dimAmount must be within [0, 1]" } }
}
