package io.github.neoruaa.glasmio.backdrop

import io.github.neoruaa.glasmio.glass.NavigationGlassMaterial
import io.github.neoruaa.glasmio.glass.navigationGlassGeometryProfile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedGlassRecipeTest {
    @Test
    fun sameMaterialProfilesDriveAllPlatforms() {
        val profile = navigationGlassGeometryProfile(44f, NavigationGlassMaterial.Clear)
        assertEquals(.75f, profile.blurRadius)
        assertEquals(18f, profile.innerHeight)
        assertEquals(32.5f, profile.innerAmount)
        assertTrue(profile.captureScale in .25f.. .5f)
    }
}
