/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/ad/HideAdsPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * DEDUPLICATION: Alternative implementation available in
 *   Binarymend/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/TruecallerPatch.kt (Remove Ads, v26.12.5)
 * Binarymend patches Loi1/baz; forcing every Z-returning method to false — broader but tied to
 * v26.12.5's obfuscation. Bufferk (kept as primary) patches after-call ad update sites directly
 * against v26.10.6 fingerprints.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerAdRemovalPatch = bytecodePatch(
    name = "Truecaller — remove ads",
    description = "No-ops after-call ad update sites (Lcom/truecaller/acs/ui/baz;->Rh and Ltw1/f;->Th).",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            AfterCallMaybeUpdateAdFingerprint.method.addInstructions(0, "return-void")
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            NeoAcsMaybeUpdateAdFingerprint.method.addInstructions(0, "return-void")
        } catch (_: PatchException) { /* fail-soft */ }
    }
}
