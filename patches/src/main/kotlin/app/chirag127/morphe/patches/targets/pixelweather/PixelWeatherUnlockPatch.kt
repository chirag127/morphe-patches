/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * Pixel Weather unlock for non-Pixel devices (Poco F7 target).
 *
 * Composition patch — enables the 3 universal in-APK gate-bypasses for
 * com.google.android.apps.weather. Pixel Weather is Pixel-9-exclusive
 * via manifest uses-feature declaration and runtime Build.MODEL checks.
 *
 * DEFAULT = false — needs on-device confirmation. Composition of
 * fail-soft universal patches.
 */

package app.chirag127.morphe.patches.targets.pixelweather

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_WEATHER
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

@Suppress("unused")
val pixelWeatherUnlockPatch = bytecodePatch(
    name = "Pixel Weather unlock (Poco F7)",
    description = "Bypass in-APK Pixel-only gates on Pixel Weather for non-Pixel devices.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_WEATHER)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch. Universal deps are fail-soft.
    }
}
