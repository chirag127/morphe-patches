/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * Pixel Screenshots unlock for non-Pixel devices (Poco F7 target).
 *
 * Composition patch — enables the 3 universal in-APK gate-bypasses for
 * com.google.android.apps.pixel.agent (Pixel Screenshots). Pixel-8+
 * exclusive; uses on-device AICore + Gemini Nano for AI-searchable
 * screenshot library.
 *
 * DEFAULT = false. **Highly likely to have server-side dependencies
 * we can't bypass** — the AI indexing runs against on-device Gemini
 * Nano which is only available on Tensor G3+ chipsets. Poco F7's
 * Snapdragon 7s Gen 4 will NOT run the indexing model even with this
 * patch. Marked as experimental for that reason.
 */

package app.chirag127.morphe.patches.targets.pixelscreenshots

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_SCREENSHOTS
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

@Suppress("unused")
val pixelScreenshotsUnlockPatch = bytecodePatch(
    name = "Pixel Screenshots unlock (Poco F7) — EXPERIMENTAL",
    description = "Bypass in-APK Pixel-only gates on Pixel Screenshots. AI indexing (on-device Gemini Nano) requires Tensor G3+ and will not work on Snapdragon.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_SCREENSHOTS)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch. Universal deps are fail-soft.
    }
}
