/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/layout/HidePremiumTabPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val truecallerHidePremiumTabPatch = bytecodePatch(
    name = "Truecaller — hide Premium tab",
    description = "Hides the Premium tab from the bottom navigation bar.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            PremiumPurchaseSupportedFingerprint.let {
                // Replace iget-object with const/4 to null out the premium button;
                // filterNotNull later drops it.
                val index = it.instructionMatches[0].index
                val reg = it.instructionMatches[0].getInstruction<TwoRegisterInstruction>().registerA
                it.method.replaceInstruction(index, "const/4 v$reg, 0x0")
            }
        } catch (_: PatchException) {
            // Fingerprint mismatch. No-op.
        }
    }
}
