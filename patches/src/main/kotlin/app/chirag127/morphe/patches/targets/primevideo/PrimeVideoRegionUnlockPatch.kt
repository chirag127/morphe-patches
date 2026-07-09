/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.amazon.avod.thirdpartyclient
 * Patch: PrimeVideoRegionUnlock
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.primevideo

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PRIME_VIDEO

@Suppress("unused")
val primeVideoRegionUnlockPatch = bytecodePatch(
    name = "PrimeVideoRegionUnlock (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.amazon.avod.thirdpartyclient.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_PRIME_VIDEO)

    execute {
        // TODO: implement.
    }
}
