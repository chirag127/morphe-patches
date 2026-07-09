/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB - fingerprint discovery needed via disassemble.yml.
 *
 * Target: com.spotify.music
 * Patch: SpotifyPremiumUnlock
 *
 * TODO: run disassemble.yml on this package, grep smali for the attack
 * surface, author real fingerprint, replace stub execute body.
 */

package app.chirag127.morphe.patches.targets.spotify

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_SPOTIFY

@Suppress("unused")
val spotifyPremiumUnlockPatch = bytecodePatch(
    name = "SpotifyPremiumUnlock (STUB)",
    description = "STUB placeholder. Real behavior authored after disassemble.yml runs on com.spotify.music.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_SPOTIFY)

    execute {
        // TODO: implement.
    }
}
