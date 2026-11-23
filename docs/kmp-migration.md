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

`LiquidMenuMorphMotion` (geometry envelopes, spring integration and portal timing) has also moved intact into `glasmio-motion/commonMain`. Android's `LiquidMenuMorphSurface` retains its original renderer but now reads frames and source bounds from that shared state machine; Desktop and iOS can animate the same model. This migration does not implement offscreen backdrop capture or optical compositing outside Android.

## UIKit/SwiftUI iOS preview

`glasmio-components` now builds a static `GlasmioComponents` framework and exposes
`GlasmioComponentsViewController()` through Kotlin/Native. The controller renders
actual `commonMain` message bubbles, themes, text and sidebar vector inside a
`ComposeUIViewController`. Embed it via `ios-sample/GlasmioContentView.swift`
in an Xcode app target. To link a simulator binary on macOS run
`./gradlew :glasmio-components:linkDebugFrameworkIosSimulatorArm64`.
This is a functional shared-UI preview and **not** an iOS glass backdrop renderer.

The macOS runner is now `macos-26` with Xcode 26.4.1 pinned for Kotlin 2.4.21 compatibility, avoiding the older Xcode 16 toolchain on `macos-15`. CI also statically checks that Kotlin `commonMain` files do not import Android or Java platform APIs.

The portable native supercircle SDF shader source is now in `glasmio-optics/commonMain` as `NativeGlassSdfFunctions`. Android `GlassSdfShader` continues to compose its original source and material uniforms; no native GPU behavior is rewritten. The JNI-backed Gaussian contour cache stays in the Android renderer until a platform-independent release contract exists.

## Portable system symbols

`GlasmioSymbolIcon` is a shared Compose API backed by the original Android `SfIcon`,
native UIKit `UIImage.systemImageNamed` on iOS, and explicit Material icon fallbacks
on Desktop. Native iOS icons require no bundled SF font and preserve OS availability;
unknown or unavailable symbols use an accessible fallback. Desktop vector fallbacks
cover common names only and are not pixel-identical to SF Symbols. The original Android
icon package and its optional host-provided font remain unchanged.

## Material Design exclusion (Android + Desktop + iOS)

The core theme and shared controls no longer use Material 3 primitives. App-specific
text uses Glasmio's `GlasmioText`, `GlasmioProvideTextStyle`, `LocalGlasmioContentColor`
and semantic `GlasmioTextStyles`; Desktop and iOS demonstrate shared native panels,
buttons and UIKit-sized toggles. Android still uses the richer native glass controls.
No Material icon packs or fonts are bundled: identical Glasmio vector fallback paths
serve Android (when no host font is supplied) and Desktop. The GPU parity work is
separate; a non-glass panel is not claimed to be an optical glass equivalent.

## One G2 corner implementation on every target

The previous Desktop/iOS `RoundedCornerShape` substitutes were removed. Core now
uses `com.mocharealm.gaze:capsule:2.1.1-patch2`, a multiplatform G2 Capsule
implementation, from `commonMain` on Android, Desktop and iOS. The host theme
always resolves corner geometry from the same library and numeric radii.

`gaze-capsule` currently publishes iOS device and Apple Silicon simulator targets but not Intel `iosX64`. Glasmio's shared libraries therefore use `iosArm64` and `iosSimulatorArm64` for binary compatibility; no unavailable Intel simulator variant is declared.

The root task `verifyNoMaterialDependencies` now resolves Android and Desktop
compile/runtime dependency graphs and rejects any transitive `androidx.compose.material`,
`androidx.compose.material3` or `org.jetbrains.compose.material*` artifacts. Together
with `scripts/check_no_material.py`, this prevents both source imports and indirect
UI toolkit dependencies from silently returning. This check requires a real Gradle
resolution on CI and is not yet reported as passing.

Cross-platform foundation tests now assert native typography metrics, shared G2 generic outlines and identical Android/Desktop vector fallback identities. These are source-level assertions until Gradle CI successfully executes them; a green build is still required for merger readiness.

## Navigation3 UI isolation

Navigation3 `navigation3-ui:1.2.0` is retained solely for its scene rendering
and metadata model. Miuix UI artifacts have been removed, and the
`NavDisplayTransitionEffects` extension has been replaced with
`GlasmioNavDisplayTransitionEffects`, defined in this repository. The
Navigation3 UI dependency explicitly excludes transitive Material/Material3.
Only the custom `GlasmioNavDisplay` renders scenes; the stock material-themed
NavDisplay composable is not used by the sample. This must still pass on-device
predictive-back regression tests before merging, to detect any accidental
runtime references to excluded classes.

The Android sample's window theme no longer inherits from the Android framework
`Theme.Material` family. It uses bare `Theme.Light.NoTitleBar` / `Theme.NoTitleBar`
parents instead. The source guard also checks resource XML for Material theme ancestry
and Material widget references. This only controls the Activity window: Compose
Glasmio still owns all in-app surfaces and styling.
