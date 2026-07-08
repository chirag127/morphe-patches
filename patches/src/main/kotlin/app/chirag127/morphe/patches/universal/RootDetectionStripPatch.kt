package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_UNIVERSAL

/**
 * Neutralize root-detection methods. Common pattern:
 *   private boolean isRooted() { ... check for "Magisk", "/system/xbin/su", ... }
 * We match methods that return boolean and reference the "Magisk" string,
 * then replace their body with `return false`.
 */
@Suppress("unused")
val rootDetectionStripPatch = bytecodePatch(
    name = "Strip root detection",
    description = "Force common root-detection methods to return false. Hides Magisk/su probes at the in-app level.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_UNIVERSAL)

    execute {
        RootDetectionFingerprint.method.replaceInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}
