/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/premium/RemovePremiumUIPatch.kt.
 * NOTE: This is the one Bufferk file without a copyright header in the upstream repo.
 * License is still GPL-3.0-or-later per the repo's LICENSE + NOTICE.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerRemovePremiumUiPatch = bytecodePatch(
    name = "Truecaller — remove Premium UI (nav drawer + tab)",
    description = "Hides bottom-nav Premium tab + PremiumNavDrawerItemView (sets GONE in onAttachedToWindow).",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Hide PremiumNavDrawerItemView — force setVisibility(GONE=8) in onAttachedToWindow
            PremiumNavDrawerItemViewFingerprint.method.addInstructions(
                0,
                """
                    const/16 v0, 0x8
                    invoke-virtual {p0, v0}, Landroid/view/View;->setVisibility(I)V
                    return-void
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }
    }
}
