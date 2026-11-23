#!/usr/bin/env python3
"""Static KMP source-set policy checks; no Gradle download is required."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED_MODULES = (
    "glasmio-core",
    "glasmio-symbols",
    "glasmio-optics",
    "glasmio-motion",
    "glasmio-components",
    "glasmio-backdrop",
)

# AndroidX Compose and Kotlin stdlib are multiplatform; Android View/Java classes aren't.
PLATFORM_IMPORT = re.compile(
    r"^\s*import\s+(?:android\.|java\.|javax\.|sun\.|androidx\.activity\.)",
    re.MULTILINE,
)
PLATFORM_SYMBOL = re.compile(
    r"\b(?:asAndroidRuntimeShader|nativeCanvas|LocalView|android\.graphics\.|"
    r"android\.view\.|android\.os\.|java\.util\.)",
)
SETTINGS = (ROOT / "settings.gradle.kts").read_text(encoding="utf-8")
violations: list[str] = []
count = 0

for name in SHARED_MODULES:
    if f'":{name}"' not in SETTINGS:
        violations.append(f"{name}: missing from settings.gradle.kts")
    source_root = ROOT / name / "src" / "commonMain" / "kotlin"
    sources = sorted(source_root.rglob("*.kt"))
    if not sources:
        violations.append(f"{name}: missing commonMain Kotlin sources")
    for path in sources:
        count += 1
        content = path.read_text(encoding="utf-8")
        imports = "\n".join(line for line in content.splitlines() if line.lstrip().startswith("import "))
        # Restrict imports to executable Kotlin, not comments and documentation.
        for match in PLATFORM_IMPORT.finditer(imports):
            violations.append(f"{path.relative_to(ROOT)}: prohibited {match.group().strip()}")
        for match in PLATFORM_SYMBOL.finditer(imports):
            violations.append(f"{path.relative_to(ROOT)}: Android/JVM-only {match.group()}")
    for target in ("androidMain", "desktopMain", "iosMain"):
        if not (ROOT / name / "src" / target / "kotlin").is_dir():
            # Pure Kotlin common modules do not need a platform-specific source set.
            continue

# All Android, Desktop and iOS Glasmio G2 corners share Gaze Capsule.
# Accidentally importing the original Android-only Kyant Capsule would silently
# reintroduce two distinct shape implementations and break visual parity.
for path in ROOT.rglob("*.kt"):
    if any(part in {".git", ".gradle", "build"} for part in path.parts):
        continue
    content = path.read_text(encoding="utf-8")
    for match in re.finditer(r"^\s*import\s+com\.kyant\.capsule\.", content, re.MULTILINE):
        line = content.count("\n", 0, match.start()) + 1
        violations.append(f"{path.relative_to(ROOT)}:{line}: use com.mocharealm.gaze.capsule")

if violations:
    for entry in violations:
        print("KMP boundary violation: " + entry)
    raise SystemExit(1)
print(f"KMP source boundaries verified: {len(SHARED_MODULES)} modules, {count} common Kotlin sources")
