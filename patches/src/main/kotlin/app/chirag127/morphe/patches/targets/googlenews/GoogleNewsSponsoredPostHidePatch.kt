/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed — file placeholder only.
 *
 * Google News surfaces sponsored article cards in the feed. Concrete evidence
 * from disassembly of v-latest (2026-07-09):
 *   smali/smali/akxk.smali:6077  const-string v3, "SPONSORED_ARTICLE_AD"
 *   smali/smali/akxk.smali:6090  const-string v3, "SPONSORED_ARTICLE_AD_NO_BUTTON_SPONSORED_BADGE"
 *
 * Class `akxk` is a feed-card-type enum. The two entries above define the
 * ordinal values `gu` (0x180) and `gv` (0x181). Consumers of these values
 * are the actual card-visibility gates.
 *
 * TODO: xref `Lakxk;->gu:Lakxk;` and `Lakxk;->gv:Lakxk;` via a full-tree grep,
 * then no-op the view holder / binder that dispatches on those enum ordinals
 * (or force the card's visibility to GONE). Cheapest strategy: locate the
 * `switch`/`if-eq` on card type and inject a `goto` past the sponsored branch,
 * or override its bind method with return-void.
 *
 * Fail-soft: fingerprint miss no-ops this patch without aborting the build.
 */

package app.chirag127.morphe.patches.targets.googlenews

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GOOGLE_NEWS

@Suppress("unused")
val googleNewsSponsoredPostHidePatch = bytecodePatch(
    name = "Google News — hide sponsored article cards (STUB)",
    description = "STUB: hide SPONSORED_ARTICLE_AD feed cards. Fingerprints not yet authored.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GOOGLE_NEWS)

    execute {
        // TODO: xref akxk->gu / akxk->gv usages and no-op the card binder or
        // force the sponsored branch of the card-type switch to hide the view.
        // Fail-soft on fingerprint miss.
    }
}
