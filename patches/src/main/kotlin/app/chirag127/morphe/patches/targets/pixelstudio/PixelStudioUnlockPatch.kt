package app.chirag127.morphe.patches.targets.pixelstudio

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_STUDIO
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

/**
 * Aggregate patch: unlock Pixel Studio on non-Pixel devices.
 *
 * Combines the three universal in-APK gate-bypasses. Play Integrity
 * server-side check is NOT covered — see docs/pixel-studio.md.
 *
 * Package: com.google.android.apps.pixel.creativeassistant
 * Requires: server-side attestation to still succeed (usually via
 * TrickyStore + Pixel keybox on rooted Poco F7). Pure-patch install
 * likely fails the first server call. Verify on-device after install.
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
        // Composition patch — no extra bytecode edits beyond what the deps do.
        // Target-specific hooks (e.g. sibling package presence check) go here
        // once we disassemble the actual APK and confirm the check sites.
    }
}
