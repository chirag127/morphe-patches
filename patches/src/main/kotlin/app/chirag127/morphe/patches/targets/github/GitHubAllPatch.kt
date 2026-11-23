/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Composition patch — original work by chirag127/morphe-patches. GPL-3.0-or-later.
 */

package app.chirag127.morphe.patches.targets.github

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GITHUB_ANDROID

/**
 * Aggregate patch that enables the "water" AMOLED + telemetry-strip bundle for the
 * closed-source GitHub Android app (`com.github.android`).
 *
 * Scope: strictly conservative. UI theming + no-op analytics. No network interception.
 * No account-flow tampering. No feature unlocks.
 *
 * Composed atomic patches:
 *   1. githubAmoledDarkThemePatch          — palette override to true black (STUB)
 *   2. githubAmoledCommentSurfacePatch     — extend AMOLED to comment cards (STUB)
 *   3. githubTelemetryStripPatch           — no-op Firebase Analytics (STUB)
 *
 * NOT composed here (opt-in via user's own toggling because they're speculative UX):
 *   - githubExtendedCommentFeaturesPatch   — future comment-UX enhancements (STUB, default = false)
 *
 * Fail-soft posture: each depended-on patch swallows its own PatchException so a
 * version-drift miss on one no-ops that entry without aborting the build.
 */
@Suppress("unused")
val githubAllPatch = bytecodePatch(
    name = "GitHub — all patches",
    description = "Enables AMOLED theme + comment surface + telemetry strip. Water-only; no behavior or network changes.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_GITHUB_ANDROID)

    dependsOn(
        githubAmoledDarkThemePatch,
        githubAmoledCommentSurfacePatch,
        githubTelemetryStripPatch,
    )

    execute {
        // Composition patch. All real work happens in the depended-on atomic patches.
    }
}
