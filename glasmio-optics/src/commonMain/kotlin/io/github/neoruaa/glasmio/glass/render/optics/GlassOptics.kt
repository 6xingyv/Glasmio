package io.github.neoruaa.glasmio.glass.render.optics

import kotlin.math.sqrt

/**
 * CPU reference of the scalar optical transfer in [NativeGlassRefractionFunctions].
 * Useful for KMP cross-target calibration and numerical tests; it does not perform
 * backdrop sampling or GPU rasterization.
 */
object GlassOptics {
    /** Signed iOS-like edge displacement in source-texture units. */
    fun displacement(signedDistance: Float, inverseHeight: Float, signedAmount: Float): Float {
        require(inverseHeight > 0f && inverseHeight.isFinite())
        require(signedDistance.isFinite() && signedAmount.isFinite())
        val u = (-signedDistance * inverseHeight).coerceIn(0f, 1f)
        return signedAmount * (1f - sqrt(u * (2f - u)).coerceIn(0f, 1f))
    }

    /** Refraction fade, including the shader's step-at-threshold behavior at zero width. */
    fun refractionWeight(
        signedDistance: Float,
        threshold0: Float,
        threshold1: Float,
        opacity: Float,
    ): Float {
        require(signedDistance.isFinite() && threshold0.isFinite() && threshold1.isFinite() && opacity.isFinite())
        val width = threshold1 - threshold0
        val fraction = if (width != 0f) ((signedDistance - threshold0) / width).coerceIn(0f, 1f)
            else if (signedDistance >= threshold1) 1f else 0f
        return opacity * fraction
    }
}
