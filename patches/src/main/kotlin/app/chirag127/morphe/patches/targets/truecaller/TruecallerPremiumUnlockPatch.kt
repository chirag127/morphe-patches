/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/premium/TruecallerPremiumPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * DEDUPLICATION: Alternative implementation available in
 *   bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/premium/MockPremiumPatch.kt
 * Bufferk patches AttributesDTO.<init>, k.b(), k.S1(), k.c1() and PremiumState ctor.
 * Paresh (kept as primary) is more comprehensive: also blocks server state overwrite
 * (SavePremiumStateFingerprint) and subscription-expired notification worker, which
 * prevents server-side revocation from resetting local unlock state.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerPremiumUnlockPatch = bytecodePatch(
    name = "Truecaller — Premium unlock",
    description = "Unlocks premium features (GOLD tier + all features + block server revocation + block expired-notification worker).",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Always return true for isPremium check
            IsPremiumFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            // Return GOLD tier instead of FREE
            GetPremiumTierFingerprint.method.addInstructions(
                0,
                """
                    sget-object v0, Lcom/truecaller/premium/data/tier/PremiumTierType;->GOLD:Lcom/truecaller/premium/data/tier/PremiumTierType;
                    return-object v0
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            // All features available — unlocks Max blocking, 140 series, telemarketers etc.
            IsFeatureAvailableFingerprint.method.addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            // Block server from overwriting local premium state (prevents Max blocking reset)
            SavePremiumStateFingerprint.method.addInstructions(
                0,
                """
                    sget-object p1, Lkotlin/Unit;->a:Lkotlin/Unit;
                    return-object p1
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }

        try {
            // Block subscription-expired notification worker
            SubscriptionNotificationWorkerFingerprint.method.addInstructions(
                0,
                """
                    new-instance p1, Landroidx/work/qux${'$'}bar${'$'}qux;
                    invoke-direct {p1}, Landroidx/work/qux${'$'}bar${'$'}qux;-><init>()V
                    return-object p1
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }
    }
}
