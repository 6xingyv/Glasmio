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

## Toolchain compatibility

Kotlin 2.4.21 is tested with AGP through 9.3.1, not the previous AGP 9.4.1.
The build now pins AGP 9.3.1 (which supports SDK 37), together with Gradle 9.6.
The core and symbol catalog additionally declare iosArm64, iosSimulatorArm64 and iosX64;
use the macOS CI job for native compilation. Android and Desktop builds remain mandatory.

## Shared Liquid Glass optics

The `glasmio-optics` KMP module owns the signed-displacement and spectral-refraction
shader source plus a CPU reference scalar transfer, with cross-platform tests.
Android `glasmio-glass` consumes it as an implementation dependency and continues
using the exact existing AGSL/RenderEffect/JNI path. **Shader source sharing is not
GPU backend portability**: Desktop and iOS still need actual Skia/Metal renderers.

The native control's material presets and geometry profiles now also live in `glasmio-optics/commonMain`. These maintain the original package names and computed parameters, while Android owns the actual material rendering.

## Shared motion

`glasmio-motion` now contains the original iOS alert, context menu, header, sheet drag, and navigation transition motion solvers. It preserves their Kotlin packages for Android call sites; the Android UI and Scaffold modules depend on it while Desktop/iOS can run its motion primitives without the Android UI layers. Unit tests verify endpoint behavior. The visual UI is still Android-only.

## CI and release safety

The Android consumer verifier now uses AGP 9.3.1, consistent with the build. CI executes core Android host tests in addition to Desktop/iOS compilation. The old release bundle logic recognizes only six Android AARs, so tagged releases are intentionally blocked before signing or upload until publication artifacts and legal notices can be verified for all KMP variants. **Do not bypass this guard to publish.**

## Desktop entry point

A runnable `desktop-sample` JVM application now consumes the shared public API.
Build with `./gradlew :desktop-sample:compileKotlin` and launch via
`./gradlew :desktop-sample:run` on a workstation with a graphical desktop.
It displays colors, typography, material profile constants and numeric optical
samples, **not** a simulated or faithful Desktop Liquid Glass renderer.

## Real cross-platform controls

`glasmio-components` now owns the original ChatKit-style message bubble shape and
`GlasmioMessageBubble` compositor plus the SF-sidebar toggle vector; unlike numerical
previews, these render genuine shared Compose UI on Android, Desktop, and iOS. Android
`glasmio-ui` re-exports the component dependency so previously imported package names
and call sites remain intact. Desktop preview draws the shared controls. Platform-specific
backdrop refraction and native overlays are outside this module.

The indicator deform/anchor geometry is now portable in `glasmio-optics`, and text-selection editing overlay placement lives in `glasmio-components`. The Android glass module exports optics transitively as an API dependency so its public material and indicator types stay accessible to existing UI consumers.
