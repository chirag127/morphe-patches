/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/premium/HidePremiumUpgradePromptsPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * Safety-net for MockPremium — same finish()/return injection into paywall onCreate.
 * Kept as separate patch so users can enable Premium unlock without also killing
 * every paywall entry point, or vice versa.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerHidePremiumUpgradeUiPatch = bytecodePatch(
    name = "Truecaller — hide Premium upgrade UI",
    description = "Kills paywall activity onCreate as a safety-net when Premium unlock is enabled.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            FullScreenPaywallOnCreateFingerprint.method.addInstructions(
                0,
                """
                    invoke-virtual {p0}, Landroid/app/Activity;->finish()V
                    return-void
                """,
            )
        } catch (_: PatchException) { /* fail-soft */ }
    }
}
