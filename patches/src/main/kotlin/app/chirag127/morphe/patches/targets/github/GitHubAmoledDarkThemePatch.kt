/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * Conceptually modeled on hoo-dles/morphe-patches AmoledPatch.kt
 * (patches/src/main/kotlin/hoodles/morphe/patches/github/misc/theme/AmoledPatch.kt) —
 * that patch is a two-part bytecodePatch+resourcePatch targeting v1.255.0. We do NOT
 * copy its bytecode fingerprints verbatim because this repo has no `resourcePatch` API
 * precedent yet (Truecaller/PixelStudio targets all use `bytecodePatch` only) and the
 * upstream `AmoledPatch.kt` source was not fetched offline for verbatim adaptation.
 *
 * STUB — fingerprint & resource-XML override discovery needed. File is a fail-soft
 * placeholder wired into `github-all` so composition doesn't dangle.
 *
 * INTENT: rewrite the dark palette in `res/values-night/colors.xml` (+ `values-night-v31/`
 * dynamic-color variant + `assets/webview/colors_dark.css`) so background surfaces become
 * true black `#FF000000`. Workflow disassembly (see
 * chirag127/morphe-patches Action run 28978163106, artifact `disassembled-com.github.android`)
 * confirms the color anchors:
 *
 *   res/values-night/colors.xml entries to rewrite to #FF000000:
 *     - appBackground         (currently #FF050505)
 *     - backgroundPrimary     (currently #FF050505)
 *     - backgroundSecondary   (currently #FF17181C)
 *     - backgroundElevatedPrimary   (currently #FF1F1F24 == gray_850)
 *     - backgroundElevatedSecondary (currently #FF2E2F37)
 *     - backgroundElevatedTertiary  (currently #FF383A42)
 *     - backgroundInset       (per hoodles AmoledPatch)
 *     - gradientHeaderBackgroundStart (per hoodles AmoledPatch)
 *
 *   res/values/colors.xml (grayscale ramp fallback):
 *     - gray_1000 (#FF050505 -> #FF000000)
 *     - gray_900  (#FF17181C -> #FF000000, optional)
 *     - gray_850  (#FF1F1F24 -> #FF000000, optional)
 *
 *   assets/webview/colors_dark.css : rewrite background tokens.
 *
 * Bytecode alt-path (per hoodles) — override GitHubFunctionalColors constructor
 * arg positions {3, 11, 119} to 0xFF00000000000000L via a fingerprint on the
 * class-name string. Reserved for a future `bytecodePatch` companion once the
 * resource-patch API is either wired into this repo or the bytecode approach is
 * fingerprinted against v1.255.x locally.
 *
 * TODO:
 *   1. Confirm whether `app.morphe.patcher.patch.resourcePatch` is exposed in this
 *      patcher API version (Truecaller uses only bytecodePatch — no precedent in
 *      chirag127/morphe-patches as of 2026-07-09). If exposed, port the resource
 *      overrides above.
 *   2. Alternatively, author a bytecodePatch mirroring hoo-dles's approach:
 *      fingerprint the `"GitHubFunctionalColors(backgroundPrimary="` string ctor site,
 *      rewrite the color-long args to 0xFF00000000000000L.
 *   3. Verify on emulator via the existing `verify.yml` matrix once the resource
 *      overrides are in place — smoke check that the app still boots (background
 *      swap is purely visual and should not affect boot).
 */

package app.chirag127.morphe.patches.targets.github

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GITHUB_ANDROID

@Suppress("unused")
val githubAmoledDarkThemePatch = bytecodePatch(
    name = "GitHub — AMOLED dark theme (STUB)",
    description = "STUB: rewrite dark-theme backgrounds to true-black #000000. Resource-XML overrides pending; see file-level TODO.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GITHUB_ANDROID)

    execute {
        // TODO: once resourcePatch is wired or bytecode fingerprints authored,
        // implement palette override per file-level comment. Fail-soft no-op today.
    }
}
