/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/layout/HidePremiumSettingsPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val truecallerHidePremiumFromSettingsPatch = bytecodePatch(
    name = "Truecaller — hide Premium from settings",
    description = "Hides the Premium options from the settings and user details pages.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Hide premium item from settings categories page
            SettingsPremiumVisibilityFingerprint.method.apply {
                val instructions = implementation!!.instructions.toList()
                val indicesToPatch = mutableListOf<Pair<Int, Int>>()

                for (i in instructions.indices) {
                    val inst = instructions[i]
                    if (inst.opcode == Opcode.IGET_BOOLEAN) {
                        val ref = (inst as ReferenceInstruction).reference.toString()
                        if (ref.contains("Lfh2/m0;->a:Z")) {
                            val destReg = (inst as TwoRegisterInstruction).registerA
                            indicesToPatch.add(i to destReg)
                        }
                    }
                }

                for ((index, reg) in indicesToPatch.reversed()) {
                    replaceInstruction(index, "const/4 v$reg, 0x0")
                }
            }
        } catch (_: PatchException) {
            // Fingerprint mismatch on settings categories. Fall through to Compose patch.
        }

        try {
            // Hide "Premium member" block from user details/settings page (Compose UI)
            PremiumBlockComposeFingerprint.method.addInstructions(0, "return-void")
        } catch (_: PatchException) {
            // Fingerprint mismatch on Compose UI. No-op.
        }
    }
}
