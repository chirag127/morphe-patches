/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.facebook.katana
 * Patch: FacebookRemoveSponsoredPosts
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.facebook

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_FACEBOOK

@Suppress("unused")
val facebookRemoveSponsoredPostsPatch = bytecodePatch(
    name = "FacebookRemoveSponsoredPosts (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.facebook.katana.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_FACEBOOK)

    execute {
        // TODO: implement.
    }
}
