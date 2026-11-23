/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — placeholder for future comment-UX improvements — default = false.
 *
 * INTENT: opt-in UX additions to the comment surface. No behavior/network changes;
 * strictly view-composition tweaks that render existing model data more richly.
 *
 * CANDIDATE FEATURES (all are "water" — presentational only, no server round-trips
 * beyond what the app already fetches):
 *
 *   A. Reactions summary inline
 *      Show the reactions count + emoji summary next to comment metadata even when
 *      the current app collapses it behind a "..." menu. Data source is already in
 *      `com/github/service/models/response/type/ReactionContent.smali` +
 *      `smali_classes3/com/github/android/views/ReactionView.smali`. We're just
 *      un-hiding it from the compose tree.
 *
 *   B. Comment author-association badge always visible
 *      `com/github/service/models/response/type/CommentAuthorAssociation.smali`
 *      is fetched every render, but the OWNER / MEMBER / CONTRIBUTOR / FIRST_TIMER
 *      badges are only shown in specific contexts. Force-render the badge on every
 *      comment so triage is faster.
 *
 *   C. Show HideCommentReason on hidden comments
 *      `com/github/service/models/HideCommentReason.smali` is on the model but the
 *      UI often just says "This comment has been hidden." Render the reason (SPAM,
 *      OUTDATED, ABUSE, etc.) so users know whether to expand.
 *
 *   D. Compose sheet always-expanded on tablets
 *      `ComposeCommentBottomSheetDialog.smali` (only preserved-name class in the
 *      comment package) currently opens as a partial sheet. On wide screens, opening
 *      full-height first would remove one tap. Requires a screen-size check + a
 *      one-line initial-state override.
 *
 * SCOPE GUARDS (must stay true for this to remain a "water" patch):
 *   - No new network calls. Reuse whatever the app already fetched.
 *   - No account-flow changes. Reactions and badges are read-only surfaces.
 *   - No server-response tampering. Every candidate above is pure view composition.
 *   - Default = false. Opt-in only.
 *
 * TODO (in priority order):
 *   1. Pick ONE candidate feature above and prototype it. Reactions summary (A) is
 *      the most self-contained: `ReactionView.smali` is a preserved name so the
 *      fingerprint is nearly free.
 *   2. Author the fingerprint + view-tree injection in this file.
 *   3. Verify on the emulator matrix that the app boots and comments still render
 *      when the feature is toggled on.
 *   4. Once ONE candidate works, split the others into their own atomic patches
 *      per the "one concern per patch" convention observed in the Truecaller target.
 */

package app.chirag127.morphe.patches.targets.github

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GITHUB_ANDROID

@Suppress("unused")
val githubExtendedCommentFeaturesPatch = bytecodePatch(
    name = "GitHub — extended comment UX (STUB)",
    description = "STUB placeholder for opt-in comment-UX enhancements (reactions summary, author-association badge, etc.). No behavior/network changes.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GITHUB_ANDROID)

    execute {
        // TODO: pick one candidate from the file-level comment and prototype it.
        // Reactions summary is the recommended starting point (ReactionView.smali
        // is a preserved-name class — cheapest fingerprint).
    }
}
