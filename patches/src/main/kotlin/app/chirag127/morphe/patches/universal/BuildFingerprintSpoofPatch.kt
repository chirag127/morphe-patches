package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Force in-APK "is this a Pixel?" checks to return true.
 *
 * NOTE: does not touch Build.MANUFACTURER / Build.MODEL themselves —
 * those live in framework and can't be edited from within an APK.
 * Only patches call-sites INSIDE the target APK.
 *
 * Fail-soft: if the fingerprint doesn't match, log-and-continue instead
 * of aborting the patch chain.
 */
@Suppress("unused")
val buildFingerprintSpoofPatch = bytecodePatch(
    name = "Spoof Pixel build fingerprint check",
    description = "In-APK Pixel-model checks return true (fail-soft on fingerprint miss).",
    default = false,
) {
    execute {
        try {
            BuildModelPixelCheckFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // Target has no method returning boolean that literally contains
            // "Pixel". The check likely uses a different pattern (e.g. reflection,
            // or check against Build.HARDWARE, or delegation to a sibling package).
            // No-op instead of aborting.
        }
    }
}
