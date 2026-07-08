/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from:
 *   - Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/misc/DisableTelemetryPatch.kt
 *   - Binarymend/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/TruecallerPatch.kt (Disable Telemetry, v26.12.5)
 * Two upstream implementations merged: Paresh's targeted enableTracking() no-op is primary
 * since the shared fingerprint file already targets v26.10.6. Binarymend's broader "no-op every
 * method whose name contains 'log' or equals 'enableTracking'" strategy is preserved as an
 * alt-comment for future version-drift resilience.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

// Alternative implementation available in Binarymend/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/TruecallerPatch.kt
// (broader: iterates AppStartTracker methods, no-ops any method whose name contains "log" or == "enableTracking").
@Suppress("unused")
val truecallerTelemetryDisablePatch = bytecodePatch(
    name = "Truecaller — disable telemetry",
    description = "Disables AppStartTracker and other telemetry points.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Make enableTracking() a no-op — isEnabled stays false, so
            // onActivityCreate / onServiceCreate / onBroadcastReceive short-circuit.
            AppStartTrackerFingerprint.method.addInstructions(0, "return-void")
        } catch (_: PatchException) {
            // Fingerprint mismatch. No-op.
        }
    }
}
