package io.github.neoruaa.glasmio.glass.render.optics

import kotlin.test.Test
import kotlin.test.assertTrue

class NativeGlassShaderSourceTest {
    @Test
    fun sharedSdfRetainsOpticalNormalAndScalarField() {
        assertTrue(NativeGlassSdfFunctions.contains("nativeGlassSafeNormal"))
        assertTrue(NativeGlassSdfFunctions.contains("nativeGlassRoundedField"))
        assertTrue(NativeGlassSdfFunctions.contains("normalMix"))
    }

    @Test
    fun refractionShaderRetainsDisplacementAndSpectralSamples() {
        assertTrue(NativeGlassRefractionFunctions.contains("nativeGlassDisplacement"))
        assertTrue(NativeGlassSpectralFunctions.contains("nativeGlassAberrate"))
        assertTrue(CustomGlassChromaticFunctions.contains("customGlassChromaticVector"))
    }
}
