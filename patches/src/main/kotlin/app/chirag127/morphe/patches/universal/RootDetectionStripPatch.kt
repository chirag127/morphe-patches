package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Neutralize root-detection utility methods.
 *
 * Narrow fingerprint: PUBLIC STATIC boolean method referencing "Magisk"
 * string. Utility-method shape only.
 */
@Suppress("unused")
val rootDetectionStripPatch = bytecodePatch(
    name = "Strip root detection (narrow)",
    description = "Force public static boolean methods referencing \"Magisk\" to return false. Narrow-scoped.",
    default = false,
) {
    execute {
        try {
            RootDetectionCheckerFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // No matching root-check utility method.
            // No-op.
        }
    }
}
