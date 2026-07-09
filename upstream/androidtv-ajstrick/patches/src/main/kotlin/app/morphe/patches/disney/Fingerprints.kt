/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/disneyplus/ads/Fingerprints.kt
 * ALL CREDIT GOES TO RookieEnough FOR THE ORIGINAL CODE
 *
 * Updated for Disney+ Android TV v26.8.0+rc6 (versionCode 1779314460)
 * - InsertionGetPointsFingerprint:  VALIDATED ✅  (class + method unchanged)
 * - InsertionGetRangesFingerprint:  VALIDATED ✅  (class + method unchanged)
 * - PauseAdStartedFingerprint:      NEW ✅  targets MediaXPauseSession.started()
 *
 * Pause ad patch history:
 *   v1 — targeted onPauseScheduled() → no match (wrong method)
 *   v2 — targeted createPauseSession() → matched but wrong layer;
 *         prod-frida-origin.bamgrid.com still reached (pcap confirmed)
 *   v3 — targeted MediaXPauseSession.started() → blocks Glide fetch at
 *         source; confirmed via pcap that this is where the image request
 *         originates (prod-frida-origin.bamgrid.com fires at +26.10s)
 */

package app.morphe.patches.disney

import app.morphe.patcher.Fingerprint

// ---------------------------------------------------------------------------
// Existing fingerprints — validated present and structurally unchanged in
// com.dss.sdk.internal.media.Insertion as of v26.8.0+rc6-2026.05.20
// ---------------------------------------------------------------------------

internal object InsertionGetPointsFingerprint : Fingerprint(
    returnType = "Ljava/util/List",
    custom = { method, _ ->
        method.name == "getPoints" &&
            method.definingClass == "Lcom/dss/sdk/internal/media/Insertion;"
    },
)

internal object InsertionGetRangesFingerprint : Fingerprint(
    returnType = "Ljava/util/List",
    custom = { method, _ ->
        method.name == "getRanges" &&
            method.definingClass == "Lcom/dss/sdk/internal/media/Insertion;"
    },
)

// ---------------------------------------------------------------------------
// Pause ad fingerprint — MediaXPauseSession.started()
//
// Network analysis (PCAPdroid) confirmed the pause ad image is fetched from
// prod-frida-origin.bamgrid.com at +26.10s after pause, triggered by a
// Glide image load inside MediaXPauseSession.started(). This is the correct
// interception point — earlier in the chain (createPauseSession, onPauseScheduled)
// does not prevent the network fetch.
//
// Smali:
//   const-string v0, "pauseData"                   ← Kotlin null-check label
//   invoke-static v2, v0, kotlin/jvm/internal/k->g
//   invoke-virtual v2, MediaXPause;->into()Lbv0/m; ← builds Glide render model
//   iput-object v2, v1, ...->pause                 ← stores model
//   getStarted().onNext(kotlin/Unit)               ← triggers overlay render
//
// Patching strategy: return-void at offset 0.
//   - into() is never called → no Glide request → no image fetched
//   - prod-frida-origin.bamgrid.com is never contacted
//   - beacons.digital.disneyadvertising.com impression beacon never fires
//   - ad.doubleclick.net measurement never fires
//   - Overlay render event never published
//
// Anchor: "pauseData" is a Kotlin compiler null-check label derived from the
// source parameter name — stable across ProGuard minification.
// The class and method names are unobfuscated source-level names.
// ---------------------------------------------------------------------------

internal object PauseAdStartedFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("pauseData"),
    custom = { method, _ ->
        method.name == "started" &&
            method.definingClass ==
                "Lcom/disneystreaming/nve/player/mel/MediaXPauseSession;"
    },
)
