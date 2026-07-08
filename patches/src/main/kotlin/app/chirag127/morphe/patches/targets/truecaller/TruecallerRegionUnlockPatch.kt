/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed via disassemble workflow — file placeholder only.
 *
 * Intent: unlock region-locked features (India-only spam blocking tiers, SDR/OTP
 * shortcodes, government-directory lookups) on non-Indian SIM cards.
 *
 * TODO: disassemble v26.10.6, grep for:
 *   - "TelephonyManager.getSimCountryIso", "getNetworkCountryIso"
 *   - "country_code" / "region" preference reads
 *   - feature-flag gates like "isIndiaRegion", "isFraudInsuranceAvailable"
 * Then rewrite region getters to always return "IN" (or the region with the
 * broadest feature set), OR rewrite each isRegionX check to return true.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerRegionUnlockPatch = bytecodePatch(
    name = "Truecaller — region unlock (STUB)",
    description = "STUB: unlock region-locked features. Fingerprints not yet discovered.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        // TODO: implement once region-gate fingerprints are authored.
        // See file-level comment for grep starting points.
    }
}
