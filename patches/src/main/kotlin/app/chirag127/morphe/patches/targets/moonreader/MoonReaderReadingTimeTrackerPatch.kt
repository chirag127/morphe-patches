/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.flyersoft.moonreaderp
 * Patch: MoonReaderReadingTimeTracker
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.moonreader

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_MOON_READER

@Suppress("unused")
val moonReaderReadingTimeTrackerPatch = bytecodePatch(
    name = "MoonReaderReadingTimeTracker (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.flyersoft.moonreaderp.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_MOON_READER)

    execute {
        // TODO: implement.
    }
}
