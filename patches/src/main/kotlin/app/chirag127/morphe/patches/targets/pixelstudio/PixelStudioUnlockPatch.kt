package app.chirag127.morphe.patches.targets.pixelstudio

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_STUDIO
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

/**
 * Aggregate patch: unlock Pixel Studio on non-Pixel devices.
 *
 * Composes the three universal in-APK gate-bypasses via dependsOn.
 * Because each dependency is fail-soft, a fingerprint miss on any single
 * universal patch WILL NOT abort the target-patch chain.
 *
 * Package: com.google.android.apps.pixel.creativeassistant
 * Server-side Play Integrity is NOT bypassed. Pure-patch install may still
 * fail on first server call. Verify on-device.
 */
@Suppress("unused")
val pixelStudioUnlockPatch = bytecodePatch(
    name = "Pixel Studio unlock (Poco F7)",
    description = "Bypass in-APK Pixel-only gates: hasSystemFeature, Pixel model check, root detection. Server-side Play Integrity NOT bypassed.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_STUDIO)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch — no extra bytecode edits.
        // Universal dependencies are fail-soft: each catches its own
        // PatchException so a miss on one doesn't abort the chain.
        // Add target-specific hooks here once actual APK is disassembled.
    }
}
