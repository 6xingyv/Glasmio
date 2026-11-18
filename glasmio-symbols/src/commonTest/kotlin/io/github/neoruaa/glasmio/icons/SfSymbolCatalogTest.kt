package io.github.neoruaa.glasmio.icons

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SfSymbolCatalogTest {
    @Test fun enumNamesResolveToExactAddresses() {
        for (symbol in SfSymbol.entries) {
            assertEquals(symbol.codePoint, SfSymbolCatalog.codePointOrNull(symbol.systemName))
        }
    }
    @Test fun lookupDoesNotSynthesizeUnknownSymbols() {
        assertTrue(SfSymbolCatalog.systemNames.size >= 8_000)
        assertEquals(0x10039E, SfSymbolCatalog.codePoint("house"))
        assertNull(SfSymbolCatalog.codePointOrNull("not.a.real.sf.symbol"))
    }
}
