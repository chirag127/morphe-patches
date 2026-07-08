/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from bufferk/morphe-patches/patches/src/main/kotlin/app/morphe/patches/truecaller/premium/HideGetPremiumBannersPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerHidePremiumBannersPatch = bytecodePatch(
    name = "Truecaller — hide Get Premium banners",
    description = "Immediately finishes the full-screen paywall activity so promotional banners never render.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            // Inject Activity.finish() + return-void at the start of onCreate,
            // AFTER super.onCreate() would have run. Safer variant: return-void
            // pattern used by Bufferk relies on the framework tolerating an early return.
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
