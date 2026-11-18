# KMP migration: foundation

Glasmio is migrating incrementally to Compose Multiplatform. **This branch migrates glasmio-core and the SF Symbols catalog**, not the Liquid Glass renderer or UI modules.

The shared `commonMain` source set contains the color, typography, theme, spacing and gesture-exclusion APIs. Android retains the original Capsule continuous rounded-rectangle shapes and display-cutout repair. Desktop substitutes Compose rounded corners and a no-op cutout repair. The public Kotlin package is unchanged.

The `glasmio-symbols` module shares the SF Symbols catalog and enum on Android/Desktop; `glasmio-icons` still owns Android-specific glyph drawing and fallbacks. Other modules (`glass`, `icons`, `ui`, `scaffold`, `preference`) remain Android-only. In particular, Android RuntimeShader, RenderNode, C++/JNI, typography glyph rasterization and platform overlays are not available on Desktop yet. Do not describe Desktop Liquid Glass rendering as supported.

## Verification

With JDK 21 and Android SDK 37:

```sh
./gradlew :glasmio-core:compileKotlinDesktop :glasmio-core:desktopTest :glasmio-symbols:desktopTest
./gradlew :sample:assembleDebug
```

## Release blocker

The new `glasmio-symbols` module also publishes metadata and platform artifacts. KMP emits a root metadata publication plus platform-specific artifacts. The existing Central staging and `scripts/release_bundle.py` presume six Android AAR coordinates. Publishing this branch before adapting the bundle validator, source/legal packaging and downstream-consumer tests is **not supported**. Android regression and desktop compilation tests must pass before merging.

## Next phases

1. Split the Android GPU implementation into a platform backend and a common renderer interface.
2. Migrate shared geometry, gestures and composables, retaining Android-specific native paths.
3. Introduce platform font and icon implementations.
4. Port UI/scaffold and their navigation/overlay dependencies.
5. Add Apple targets, platform rendering, tests, and target-aware Maven Central release.
