/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/premium/MockPremiumPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * DEDUPLICATION note: Paresh's TruecallerPremiumUnlockPatch is the canonical premium
 * unlock in this bundle. This "MockPremiumBadgePatch" is a NARROWER surface — it only
 * paints the Premium badge/UI-state, without hijacking feature-gating or server-state
 * writes. Kept because a user may want the badge cosmetic without the full unlock.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerMockPremiumBadgePatch = bytecodePatch(
    name = "Truecaller — mock Premium badge only",
    description = "Paints the Premium badge in UI without unlocking features (cosmetic-only variant of Premium unlock).",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Same as Premium unlock's isPremium — this gives the badge painting a positive read.
            IsPremiumFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            // GOLD tier for the badge tint
            GetPremiumTierFingerprint.method.addInstructions(
                0,
                """
                    sget-object v0, Lcom/truecaller/premium/data/tier/PremiumTierType;->GOLD:Lcom/truecaller/premium/data/tier/PremiumTierType;
                    return-object v0
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }
    }
}
