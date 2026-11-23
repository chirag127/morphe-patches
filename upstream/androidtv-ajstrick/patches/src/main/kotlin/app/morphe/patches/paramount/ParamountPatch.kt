/*
 * Paramount+ Android TV — Ad Suppression Patch
 *
 * Validated against:
 *   v16.8.0  (versionCode 520000464) — com.cbs.ott
 *   v16.12.0 (versionCode 520000571) — com.cbs.ott
 *
 * Coverage:
 *   ✅ VOD SSAI ads  — createVodStreamRequest() returns empty zzcx →
 *                      DAI fails → bundled IMA SDK reports an
 *                      AdErrorType.LOAD/AdErrorCode.INTERNAL_ERROR failure
 *                      via the app's own onAdError() listener (unmodified
 *                      LOAD/PLAY filter already forwards it) →
 *                      ErrorCriticalEvent → nm0.c-equivalent fallback →
 *                      content plays from cbsaavideo.com without SSAI ads
 *   ✅ Pause ads     — CbsPauseWithAdsOverlay state machine
 *   ➡️ Live TV       — DAI untouched (live TV has no fallback content URL)
 *
 * NOTE ON onAdError(): an earlier revision of this patch additionally
 * deleted the LOAD/PLAY type filter inside onAdError() (class Luk0; in
 * v16.8.0 / Lcl0; in v16.12.0), believing the empty-StreamRequest failure
 * fell outside that filter on v16.12.0. Bytecode tracing of the bundled IMA
 * SDK showed otherwise: requestStream() never throws synchronously in either
 * version, and the SDK's own async failure path (zzq.zza(Throwable)) always
 * classifies the resulting error as AdErrorType.LOAD, which the *unmodified*
 * filter already forwards to the fallback. Deleting the filter instead
 * forwarded every onAdError() call — including benign, non-fatal ad errors
 * unrelated to our induced empty request — straight into the critical-error
 * teardown path, killing otherwise-working VOD playback (spinner, then a
 * dark screen). That filter removal has been reverted; only Patches 1 and 2
 * below remain.
 */

package app.morphe.patches.paramount

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities

@Suppress("unused")
val paramountPatch = bytecodePatch(
    name = "Paramount+ Android TV",
    description = "Removes VOD ads and pause ads while preserving live TV.",
) {
    compatibleWith(AppCompatibilities.PARAMOUNT_TV)

    execute {
        // ------------------------------------------------------------------
        // Patch 1: VOD SSAI — createVodStreamRequest (3-arg and 4-arg)
        //
        // Returns a valid but empty zzcx StreamRequest. No contentSourceId,
        // videoId, or apiKey setters are called. Unchanged from v16.8.0.
        // The bundled IMA SDK reports the resulting failure as
        // AdErrorType.LOAD, which the app's own (unmodified) onAdError()
        // listener already forwards to the AVIA ErrorCriticalEvent fallback.
        // ------------------------------------------------------------------
        arrayOf(
            VodStreamRequest3ArgFingerprint,
            VodStreamRequest4ArgFingerprint,
        ).forEach { fingerprint ->
            fingerprint.method.addInstructions(
                0,
                """
                    new-instance v0, Lcom/google/ads/interactivemedia/v3/impl/zzcx;
                    sget-object v1, Lcom/google/ads/interactivemedia/v3/internal/zzafv;->zzd:Lcom/google/ads/interactivemedia/v3/internal/zzafv;
                    invoke-direct {v0, v1}, Lcom/google/ads/interactivemedia/v3/impl/zzcx;-><init>(Lcom/google/ads/interactivemedia/v3/internal/zzafv;)V
                    return-object v0
                """.trimIndent(),
            )
        }

        // ------------------------------------------------------------------
        // Patch 2: Pause ads — CbsPauseWithAdsOverlay state machine
        //
        // Independent of IMA DAI. return-void prevents Glide image fetch,
        // alpha fade-in, and overlay render. Overlay stays at alpha=0.
        // Unchanged from v16.8.0.
        // ------------------------------------------------------------------
        PauseAdOverlayFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent(),
        )
    }
}
