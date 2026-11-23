/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed — file placeholder only.
 *
 * Google News (com.google.android.apps.magazines) does NOT use the standard
 * public AdMob entry points (AdRequest.Builder, InterstitialAd, AdView, loadAd)
 * in its own code. Disassembly of v-latest (2026-07-09) shows only:
 *   - com.google.android.gms.ads.adshield  — ad integrity SDK (not user-facing ads)
 *   - com.google.android.gms.ads.signalsdk — bidding signals
 *   - com.google.ads.interactivemedia.v3   — IMA SDK (video/pause ads)
 *   - Native feed cards keyed by enum entries `SPONSORED_ARTICLE_AD` and
 *     `SPONSORED_ARTICLE_AD_NO_BUTTON_SPONSORED_BADGE` in obfuscated class `akxk`.
 *
 * TODO: trace consumers of `akxk->gu` and `akxk->gv` (the SPONSORED_ARTICLE_AD
 * enum instances) via smali xref. Likely candidates:
 *   - Feed card factory / view holder binder that dispatches by card type
 *   - Ad-loader init method that populates feed with those enum values
 * Once located, no-op the binder path (return early with a hidden view) or
 * short-circuit the ad-loader init to a no-op.
 *
 * Also consider fingerprinting IMA SDK `AdViewData` and `PauseAdData` builders
 * in smali_classes2/com/google/ads/interactivemedia/v3/impl/data/ to no-op
 * pause / video overlay ad construction for the video player.
 *
 * Fail-soft per project convention: every fingerprint miss swallows
 * PatchException so the build still succeeds.
 */

package app.chirag127.morphe.patches.targets.googlenews

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GOOGLE_NEWS

@Suppress("unused")
val googleNewsAdRemovalPatch = bytecodePatch(
    name = "Google News — remove ads (STUB)",
    description = "STUB: no-op ad-loader init methods for the news feed. Fingerprints not yet authored.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GOOGLE_NEWS)

    execute {
        // TODO: no-op the ad-loader init once fingerprints for the akxk
        // SPONSORED_ARTICLE_AD enum-consumer methods are authored. Fail-soft.
    }
}
