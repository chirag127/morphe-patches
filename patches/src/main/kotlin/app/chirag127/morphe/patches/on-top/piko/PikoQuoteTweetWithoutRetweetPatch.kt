/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.twitter.android
 * Patch: PikoQuoteTweetWithoutRetweet
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.on_top.piko

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TWITTER

@Suppress("unused")
val pikoQuoteTweetWithoutRetweetPatch = bytecodePatch(
    name = "PikoQuoteTweetWithoutRetweet (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.twitter.android.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TWITTER)

    execute {
        // TODO: implement.
    }
}
