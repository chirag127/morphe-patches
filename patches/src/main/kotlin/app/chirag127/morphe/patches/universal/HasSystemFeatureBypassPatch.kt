package app.chirag127.morphe.patches.universal

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.PatchException

/**
 * Force Pixel-check utility methods to return true.
 *
 * Narrow fingerprint: requires PUBLIC STATIC method returning Z with
 * single String param that calls PackageManager.hasSystemFeature. This
 * shape is typical of utility methods like `isPixelDevice(String feature)`,
 * NOT Activity.onCreate / Application.onCreate (which return void and
 * take Bundle).
 *
 * Fail-soft: fingerprint miss = no-op instead of aborting chain.
 * Prior version (v1.0.1) had a broad fingerprint that matched an
 * Application.onCreate override and skipped super.onCreate() call,
 * causing SuperNotCalledException. Fixed here.
 */
@Suppress("unused")
val hasSystemFeatureBypassPatch = bytecodePatch(
    name = "Force hasSystemFeature true (narrow)",
    description = "Return true from public static boolean methods calling PackageManager.hasSystemFeature with a String arg. Narrow-scoped to avoid lifecycle callbacks.",
    default = false,
) {
    execute {
        try {
            HasSystemFeatureCheckerFingerprint.method.replaceInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // Target doesn't have a matching utility-method shape.
            // No-op instead of aborting chain.
        }
    }
}
