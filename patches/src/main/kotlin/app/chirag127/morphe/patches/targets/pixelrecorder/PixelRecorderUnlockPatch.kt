/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * Pixel Recorder unlock for non-Pixel devices (Poco F7 target, HyperOS 3).
 *
 * Composition patch — enables the 3 universal in-APK gate-bypasses for
 * com.google.android.apps.recorder. Each dependency is fail-soft (per
 * v1.2.0 narrow-fingerprint learning): a fingerprint miss no-ops that
 * one entry without aborting the target patch.
 *
 * DEFAULT = false — needs on-device confirmation before promoting to
 * default=true. Manifest declares `com.google.android.feature.PIXEL_2017_EXPERIENCE`
 * as required — confirmed via disassemble.yml on 2026-07-08.
 *
 * NOT covered:
 *   - Server-side Play Integrity attestation (Pixel Recorder's Gemini
 *     transcription may still refuse on Poco F7 first API call)
 *   - Google-signed Play Services signature check (needs MicroG-RE)
 */

package app.chirag127.morphe.patches.targets.pixelrecorder

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_PIXEL_RECORDER
import app.chirag127.morphe.patches.universal.buildFingerprintSpoofPatch
import app.chirag127.morphe.patches.universal.hasSystemFeatureBypassPatch
import app.chirag127.morphe.patches.universal.rootDetectionStripPatch

@Suppress("unused")
val pixelRecorderUnlockPatch = bytecodePatch(
    name = "Pixel Recorder unlock (Poco F7)",
    description = "Bypass in-APK Pixel-only gates on Pixel Recorder for non-Pixel devices. Server-side Gemini transcription NOT covered.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_PIXEL_RECORDER)

    dependsOn(
        hasSystemFeatureBypassPatch,
        buildFingerprintSpoofPatch,
        rootDetectionStripPatch,
    )

    execute {
        // Composition patch. Universal deps are fail-soft.
    }
}
