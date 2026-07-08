package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Force in-APK "is this a Pixel?" checks to return true.
 *
 * Narrow fingerprint: PUBLIC STATIC boolean method with no args that
 * references "Pixel" literal. Utility-method shape only — excludes
 * onCreate(Bundle) and other lifecycle callbacks.
 *
 * Framework Build.MANUFACTURER / Build.MODEL themselves are read from
 * OS memory and can't be patched from within an APK. Only patches
 * call-sites INSIDE the target APK.
 */
@Suppress("unused")
val buildFingerprintSpoofPatch = bytecodePatch(
    name = "Spoof Pixel model check (narrow)",
    description = "In-APK utility methods checking Pixel model return true. Narrow-scoped to avoid lifecycle callbacks.",
    default = false,
) {
    execute {
        try {
            PixelModelCheckerFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // No public-static-no-arg method with "Pixel" literal.
            // No-op.
        }
    }
}
