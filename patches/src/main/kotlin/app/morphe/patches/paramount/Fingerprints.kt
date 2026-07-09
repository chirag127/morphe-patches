/*
 * Paramount+ Android TV — Ad Patch Fingerprints
 *
 * Validated against:
 *   v16.8.0  (versionCode 520000464) — com.cbs.ott
 *   v16.12.0 (versionCode 520000571) — com.cbs.ott
 *
 * AD SUPPRESSION MECHANISM (confirmed via APK analysis of v16.8.0 and v16.12.0):
 *
 *   g1.c() builds the player media source (nm0.c) from ek0.s (content URI)
 *   BEFORE DAI initialization begins. This runs for all resource config types.
 *
 *   For VOD:      content URL is non-null → fallback media source is valid.
 *   For live TV:  no pre-DAI content URL exists → fallback media source is null.
 *
 *   STRATEGY: return an empty (but non-null) zzcx StreamRequest from
 *   createVodStreamRequest(). The bundled IMA SDK's zzah.requestStream()
 *   never throws synchronously on an empty/invalid StreamRequest in either
 *   version — it reports the failure later via the registered
 *   AdErrorListener (Luk0; in v16.8.0 / Lcl0; in v16.12.0). That listener's
 *   onAdError(AdErrorEvent) only forwards to the AVIA fallback path
 *   (Ln1;->t0(Boolean, Lml0)) when AdError.getErrorType() is LOAD or PLAY —
 *   but the SDK's own async failure path (zzq.zza(Throwable)) always
 *   classifies this failure as AdErrorType.LOAD, so the unmodified filter
 *   already forwards it. No changes to onAdError() are needed.
 */

package app.morphe.patches.paramount

import app.morphe.patcher.Fingerprint

// ---------------------------------------------------------------------------
// Patch 1a: VOD SSAI — createVodStreamRequest (3-arg)
// Unchanged from v16.8.0 — fingerprint still matches in v16.12.0.
// ---------------------------------------------------------------------------

internal object VodStreamRequest3ArgFingerprint : Fingerprint(
    returnType = "Lcom/google/ads/interactivemedia/v3/api/StreamRequest;",
    custom = { method, _ ->
        method.name == "createVodStreamRequest" &&
            method.definingClass ==
                "Lcom/google/ads/interactivemedia/v3/api/ImaSdkFactory;" &&
            method.parameterTypes.size == 3 &&
            method.parameterTypes.all { it == "Ljava/lang/String;" }
    },
)

// ---------------------------------------------------------------------------
// Patch 1b: VOD SSAI — createVodStreamRequest (4-arg)
// Unchanged from v16.8.0 — fingerprint still matches in v16.12.0.
//
// NOTE: v16.12.0's IMA SDK also adds a 5-arg overload taking a
// StreamRequest$StreamTrackingMode parameter, but it is not called anywhere
// in Paramount's app code (confirmed: the only real VOD call site,
// Lyk0;->run(), still calls the 3-arg overload). No fingerprint needed for it.
// ---------------------------------------------------------------------------

internal object VodStreamRequest4ArgFingerprint : Fingerprint(
    returnType = "Lcom/google/ads/interactivemedia/v3/api/StreamRequest;",
    custom = { method, _ ->
        method.name == "createVodStreamRequest" &&
            method.definingClass ==
                "Lcom/google/ads/interactivemedia/v3/api/ImaSdkFactory;" &&
            method.parameterTypes.size == 4 &&
            method.parameterTypes.all { it == "Ljava/lang/String;" }
    },
)

// ---------------------------------------------------------------------------
// Patch 2: Pause ads — CbsPauseWithAdsOverlay state machine
// Unchanged from v16.8.0.
// ---------------------------------------------------------------------------

internal object PauseAdOverlayFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("renderState: ", " not updating overlay."),
    custom = { method, _ ->
        method.definingClass.endsWith("CbsPauseWithAdsOverlay;")
    },
)
