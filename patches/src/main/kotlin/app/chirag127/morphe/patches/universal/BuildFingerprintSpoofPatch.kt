package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_UNIVERSAL

/**
 * Force in-APK "is this a Pixel?" checks to return true.
 *
 * Some apps do `Build.MODEL.startsWith("Pixel")` or similar. This patch
 * finds methods that string-compare against "Pixel" and return boolean,
 * and forces them to return true.
 *
 * NOTE: does not touch Build.MANUFACTURER / Build.MODEL themselves —
 * those are read from framework and can't be edited from within an APK.
 * Only patches call-sites INSIDE the target APK.
 */
@Suppress("unused")
val buildFingerprintSpoofPatch = bytecodePatch(
    name = "Spoof Pixel build fingerprint check",
    description = "In-APK Pixel-model checks return true. Server-side attestation not affected.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_UNIVERSAL)

    execute {
        BuildModelPixelCheckFingerprint.method.replaceInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """,
        )
    }
}
