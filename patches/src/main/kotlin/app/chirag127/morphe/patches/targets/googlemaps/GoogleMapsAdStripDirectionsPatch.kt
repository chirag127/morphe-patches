/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.google.android.apps.maps
 * Patch: GoogleMapsAdStripDirections
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.googlemaps

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GOOGLE_MAPS

@Suppress("unused")
val googleMapsAdStripDirectionsPatch = bytecodePatch(
    name = "GoogleMapsAdStripDirections (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.google.android.apps.maps.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GOOGLE_MAPS)

    execute {
        // TODO: implement.
    }
}
