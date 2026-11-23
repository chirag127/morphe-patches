/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Composition patch — original work by chirag127/morphe-patches. GPL-3.0-or-later.
 */

package app.chirag127.morphe.patches.targets.googlenews

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GOOGLE_NEWS

/**
 * Aggregate patch that enables every atomic Google News patch in this bundle.
 *
 * Individual patches default = false; users opting into `googleNewsAllPatch`
 * get the full stack in one click; users who prefer a smaller surface can pick
 * atomic patches individually and leave this one off.
 *
 * Notes on scope:
 * - Both atomic patches are STUBs pending fingerprint authoring against
 *   com.google.android.apps.magazines. Enabled by default so that discovery
 *   of Google News in a patched target automatically exposes the future
 *   fully-authored patches without user re-config.
 * - Every atomic patch is fail-soft (PatchException swallowed) so a version-drift
 *   fingerprint miss no-ops that one entry without aborting the build.
 */
@Suppress("unused")
val googleNewsAllPatch = bytecodePatch(
    name = "Google News — all patches",
    description = "Enables every atomic Google News patch above. Toggle individual patches to opt out.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_GOOGLE_NEWS)

    dependsOn(
        googleNewsAdRemovalPatch,
        googleNewsSponsoredPostHidePatch,
    )

    execute {
        // Composition patch. Real work happens in the depended-on atomic patches.
    }
}
