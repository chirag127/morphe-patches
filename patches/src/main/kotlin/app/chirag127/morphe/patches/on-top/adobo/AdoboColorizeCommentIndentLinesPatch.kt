/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.reddit.frontpage
 * Patch: AdoboColorizeCommentIndentLines
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.on_top.adobo

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_REDDIT

@Suppress("unused")
val adoboColorizeCommentIndentLinesPatch = bytecodePatch(
    name = "AdoboColorizeCommentIndentLines (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.reddit.frontpage.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_REDDIT)

    execute {
        // TODO: implement.
    }
}
