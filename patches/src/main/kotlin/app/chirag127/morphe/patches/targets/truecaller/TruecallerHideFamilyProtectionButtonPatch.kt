/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/layout/HideFamilyProtectionPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerHideFamilyProtectionButtonPatch = bytecodePatch(
    name = "Truecaller — hide Family Protection button",
    description = "Hides the Family Protection button from the bottom navigation bar.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Return false for family protect feature check — tab won't be added
            FamilyProtectFeatureFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """,
            )
        } catch (_: PatchException) {
            // Fingerprint mismatch. No-op.
        }
    }
}
