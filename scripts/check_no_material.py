#!/usr/bin/env python3
"""Reject use of Material Design dependencies or UI APIs anywhere in Glasmio.

This covers main, test, Android and KMP source sets; CI must not accidentally
reintroduce a Material fallback. Optical "material" is a different domain.
"""
from __future__ import annotations

import re
from pathlib import Path

root = Path(__file__).resolve().parents[1]
violations = []
code_re = re.compile(
    r"\b(?:import\s+androidx\.compose\.material(?:3)?\.|"
    r"androidx\.compose\.material(?:3)?\.|"
    r"\bMaterialTheme\b|\bExperimentalMaterial3Api\b|\bPullToRefreshDefaults\b|"
    r"\bTopAppBarScrollBehavior\b|\bTopAppBarState\b)"
)
gradle_re = re.compile(r"(?:compose\.material3|compose\.materialIcons|androidx\.compose\.material(?:3)?[:\"])")

xml_re = re.compile(r"(?:Theme\\.Material(?:3)?\\b|Theme\\.MaterialComponents\\b|com\\.google\\.android\\.material(?:[.:/]|$))")

for path in root.rglob("*"):
    if any(part in {".git", ".gradle", "build", ".idea"} for part in path.parts):
        continue
    if path.suffix not in {".kt", ".kts", ".xml"} or not path.is_file():
        continue
    code = path.read_text(encoding="utf-8")
    regex = xml_re if path.suffix == ".xml" else (gradle_re if path.suffix == ".kts" else code_re)
    for match in regex.finditer(code):
        line = code.count("\n", 0, match.start()) + 1
        violations.append(f"{path.relative_to(root)}:{line}: {match.group()}")
if violations:
    print("\n".join(violations))
    raise SystemExit(f"Found {len(violations)} forbidden Material API or dependency references")
print("Material Design exclusion verified across Kotlin sources and Gradle scripts")
