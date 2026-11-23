package ajstrick81.morphe.patches.vix.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.vix.shared.Constants

@Suppress("unused")
val skipAdsPatch = bytecodePatch(
    name = "Skip ads",
    description = "Suppresses ViX ad delivery by stopping the LuraPlayer linear ad-break " +
        "scheduler (client-side VAST) and preventing the Innovid SSAI ad overlay from mounting.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {

        // ─────────────────────────────────────────────────────────────────────
        // Hook 1 — LuraPlayer ad-break scheduler (TIME_UPDATED handler)
        //
        // The base ad analyzer's per-tick handler decides when a linear ad
        // break fires; for VOD it launches the LuraVodAdAnalyzer coroutine that
        // plays the due break. Returning void stops client-side (provider
        // "generic" VAST/VMAP) linear ads at the root, without touching the
        // media pipeline. (Live SSAI ads are Google DAI Pod Serving, stitched
        // server-side and out of scope for bytecode — see the "Override
        // certificate pinning" patch and adguard/vix_ssai_v1_0_agp.txt.)
        // ─────────────────────────────────────────────────────────────────────
        LuraAdBreakSchedulerFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )

        // ─────────────────────────────────────────────────────────────────────
        // Hook 2 — InnovidHelper.h(boolean, WebView, VideoModel, Flow)
        //
        // The entry point that mounts the Innovid SSAI ad WebView overlay
        // (carries the "innovidAd" model; dispatched from the player's
        // ad-event switch in videoplayer/z.smali). Returning void before the
        // first instruction stops the session before any WebView is attached
        // or network request is made. Safe to stub at index 0 — this is an
        // ordinary instance method, not a constructor.
        // ─────────────────────────────────────────────────────────────────────
        InnovidStartAdFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}
