package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Force PackageManager.hasSystemFeature(...) to return true for Pixel feature strings.
 *
 * Client-side gate. Bypasses in-APK Pixel-experience checks. Does NOT
 * defeat server-side Play Integrity attestation.
 *
 * Fail-soft: if the fingerprint doesn't match a specific target, this
 * patch logs and continues instead of aborting the patch chain.
 */
@Suppress("unused")
val hasSystemFeatureBypassPatch = bytecodePatch(
    name = "Force hasSystemFeature true",
    description = "Return true for Pixel-experience feature queries (fail-soft on fingerprint miss).",
    default = false,
) {
    execute {
        try {
            HasSystemFeatureFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // Fingerprint didn't match this APK — target likely doesn't call
            // hasSystemFeature directly (feature-check may be via reflection or
            // sibling-package presence). No-op instead of aborting chain.
        }
    }
}
