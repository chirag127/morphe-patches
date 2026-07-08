package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Force PackageManager.hasSystemFeature(...) to return true for Pixel feature strings.
 *
 * Client-side gate. Bypasses in-APK Pixel-experience checks. Does NOT
 * defeat server-side Play Integrity attestation.
 *
 * No compatibleWith() — universal, any APK.
 */
@Suppress("unused")
val hasSystemFeatureBypassPatch = bytecodePatch(
    name = "Force hasSystemFeature true",
    description = "Return true for Pixel-experience feature queries so apps that gate on hasSystemFeature() unlock non-Pixel devices.",
    default = true,
) {
    execute {
        HasSystemFeatureFingerprint.method.replaceInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """,
        )
    }
}
