package app.chirag127.morphe.patches.targets.pixelstudio

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_STUDIO
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

/**
 * Aggregate patch: unlock Pixel Studio on non-Pixel devices.
 *
 * v1.2+ uses narrowed fingerprints (PUBLIC STATIC boolean shape only)
 * to avoid the lifecycle-callback match that caused
 * SuperNotCalledException in v1.0.1.
 *
 * User target: Poco F7 (HyperOS 3, Android 15).
 *
 * Package: com.google.android.apps.pixel.creativeassistant
 * Client-side gate bypass ONLY. Server-side Play Integrity is NOT
 * covered — Pixel Studio's first API call to Google servers may still
 * fail. Rooted-device path via TrickyStore + Pixel keybox is not in
 * scope for this bundle.
 */
@Suppress("unused")
val pixelStudioUnlockPatch = bytecodePatch(
    name = "Pixel Studio unlock (Poco F7)",
    description = "Bypass in-APK Pixel-only gates on Poco F7. Uses narrowed fingerprints to avoid lifecycle-callback match. Server-side Play Integrity NOT bypassed.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_STUDIO)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch. Universal deps are fail-soft.
    }
}
