package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Neutralize root-detection methods. Matches methods returning boolean
 * that reference the "Magisk" string, replaces body with `return false`.
 *
 * Fail-soft: no-op on fingerprint miss.
 */
@Suppress("unused")
val rootDetectionStripPatch = bytecodePatch(
    name = "Strip root detection",
    description = "Force common root-detection methods to return false (fail-soft on fingerprint miss).",
    default = false,
) {
    execute {
        try {
            RootDetectionFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // No method matching the "Magisk"-string signature in this APK.
            // Target may probe root via SafetyNet/PlayIntegrity instead
            // (server-side, not patchable). No-op instead of aborting.
        }
    }
}
