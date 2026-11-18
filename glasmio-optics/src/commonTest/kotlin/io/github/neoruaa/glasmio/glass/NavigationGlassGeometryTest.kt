package io.github.neoruaa.glasmio.glass

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NavigationGlassGeometryTest {
    @Test fun clearMaterialRetainsMeasuredNativeProfile() {
        val p = navigationGlassGeometryProfile(50f, NavigationGlassMaterial.Clear)
        assertEquals(.75f, p.blurRadius)
        assertEquals(18f, p.innerHeight)
        assertEquals(32.5f, p.innerAmount)
        assertEquals(6.25f, p.outerHeight)
        assertEquals(.5f, p.captureScale)
    }

    @Test fun sizeGatesAndCaptureScaleAreBounded() {
        assertEquals(.5f, navigationGlassCaptureScale(50f))
        assertEquals(.25f, navigationGlassCaptureScale(200f))
        assertEquals(0f, navigationGlassSizeProgress(64f))
        assertEquals(1f, navigationGlassSizeProgress(160f))
        assertTrue(navigationGlassUsesLargeFace(129f))
    }

    @Test fun morphPreservesEndPoints() {
        val source = navigationGlassGeometryProfile(200f, NavigationGlassMaterial.Clear)
        val target = navigationGlassGeometryProfile(200f, NavigationGlassMaterial.RegularLarge)
        assertEquals(source, navigationGlassMorphProfile(200f, NavigationGlassMaterial.Clear, NavigationGlassMaterial.RegularLarge, 0f))
        assertEquals(target, navigationGlassMorphProfile(200f, NavigationGlassMaterial.Clear, NavigationGlassMaterial.RegularLarge, 1f))
    }
}
