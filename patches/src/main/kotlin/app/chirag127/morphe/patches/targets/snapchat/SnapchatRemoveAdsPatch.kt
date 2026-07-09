/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.snapchat.android
 * Patch: SnapchatRemoveAds
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.snapchat

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_SNAPCHAT

@Suppress("unused")
val snapchatRemoveAdsPatch = bytecodePatch(
    name = "SnapchatRemoveAds (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.snapchat.android.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_SNAPCHAT)

    execute {
        // TODO: implement.
    }
}
