package app.chirag127.morphe.patches.targets.pixelstudio

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_STUDIO
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

/**
 * Aggregate patch: unlock Pixel Studio on non-Pixel devices.
 *
 * ⚠️ DEFAULT-OFF as of v1.1.0. Earlier auto-application caused
 * `SuperNotCalledException` on MainActivity.onCreate because one of
 * the universal fingerprints matched an Activity lifecycle method by
 * accident and overwrote its body (skipping super.onCreate).
 *
 * ROOT CAUSE (from stack trace on Poco F7):
 *   com.google.android.apps.pixel.merlin.tiktok.app.MainActivity
 *   did not call through to super.onCreate()
 *
 * The fingerprints in this bundle are too-broad string-match filters
 * ("Pixel", "Magisk"). They can only be tightened after disassembling
 * the actual Pixel Studio APK — the `.github/workflows/disassemble.yml`
 * cloud runner does that on demand, uploads smali as workflow artifact,
 * and we iterate fingerprints against real bytecode.
 *
 * Until then, leave this OFF. Universal patches remain opt-in via
 * Manager's per-patch toggle.
 *
 * Package: com.google.android.apps.pixel.creativeassistant
 * Server-side Play Integrity is NOT bypassed anyway.
 */
@Suppress("unused")
val pixelStudioUnlockPatch = bytecodePatch(
    name = "Pixel Studio unlock (Poco F7) — EXPERIMENTAL, off by default",
    description = "⚠️ Known to cause SuperNotCalledException on MainActivity in v1.0.x. Off-by-default in v1.1.0 pending fingerprint rewrite from disassembled APK. Do not enable until v1.2+.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_STUDIO)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch — no bytecode edits.
        // Iteration blocked on APK disassembly (see .github/workflows/disassemble.yml).
    }
}
