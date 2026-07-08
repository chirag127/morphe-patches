/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed — file placeholder only.
 *
 * INTENT: extension of the AMOLED dark theme — force comment-card backgrounds
 * (issue comments, PR comments, review comments, discussion replies) to true black
 * so they don't stand out as gray tiles on an otherwise #000000 background.
 *
 * Workflow disassembly (Action run 28978163106) surfaces the attack surface:
 *
 *   Comment package: `smali/com/github/android/comment/` — 108 obfuscated single-letter
 *   classes (a..d1) plus ONE preserved name: `ComposeCommentBottomSheetDialog.smali`
 *   (with inner classes $a..$t+). That's the compose UI. The RENDER UI (issue/PR
 *   comment cards) is inside the obfuscated fanout — needs a Compose-theme or
 *   ViewHolder-background trace to pin.
 *
 *   Domain types worth cross-referencing:
 *     - com/github/service/models/HideCommentReason.smali
 *     - com/github/service/models/response/fileschanged/CommentLevelType.smali
 *     - com/github/service/models/response/type/CommentAuthorAssociation.smali
 *     - com/github/service/models/response/type/PullRequestReviewCommentState.smali
 *
 *   Comment ViewModels: viewmodels/f4.smali, h4.smali, issuesorpullrequests/c0.smali,
 *   viewmodels/s.smali, tasklist/{a,g,k}.smali, webview/viewholders/d.smali.
 *
 *   Reaction UI: views/ReactionView.smali + response/type/ReactionContent.smali.
 *
 * PATCH SURFACE (three options, resource-first preferred):
 *   1. Resource-XML: identify the specific color token used by comment cards
 *      (likely `backgroundElevatedPrimary` or a sibling — inspect
 *      `res/layout/comment_*.xml` + `res/color/*.xml`) and force to #FF000000.
 *      Depends on the same `resourcePatch` API bring-up as AmoledDarkThemePatch.
 *   2. WebView CSS: comment bodies with markdown render via `assets/webview/*.css`
 *      + `assets/webview/markdown.css`; rewrite `--tr-alt-background` and
 *      `--comment-background` variables to `#000` (pattern hoodles used).
 *   3. Bytecode: fingerprint the ViewHolder background-setter in
 *      `smali/com/github/android/comment/` and rewrite the color-int arg.
 *
 * TODO:
 *   1. Depends on `githubAmoledDarkThemePatch` landing real work first — this
 *      is the "one step deeper" refinement.
 *   2. Grep the disassembled tree for `setBackgroundColor` calls in the comment
 *      package once resource overrides are in place — anything the palette rewrite
 *      missed will be a hard-coded color-int in code.
 */

package app.chirag127.morphe.patches.targets.github

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GITHUB_ANDROID

@Suppress("unused")
val githubAmoledCommentSurfacePatch = bytecodePatch(
    name = "GitHub — AMOLED comment surface (STUB)",
    description = "STUB: extend AMOLED theme to comment-card backgrounds. Depends on base AMOLED patch; ViewHolder fingerprints pending.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GITHUB_ANDROID)

    dependsOn(githubAmoledDarkThemePatch)

    execute {
        // TODO: implement once base AMOLED palette overrides land and
        // comment-card ViewHolder fingerprints are authored.
    }
}
