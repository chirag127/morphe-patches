package ajstrick81.morphe.patches.primevideo.speed

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

// ─────────────────────────────────────────────────────────────────────────────
// NOTE: hoodles' original fingerprints targeted
// com.amazon.video.sdk.stores.overlays.settings.* — that package exists ONLY in
// the Prime Video PHONE app (com.amazon.avod.thirdpartyclient). The ATV build
// (com.amazon.amazonvideo.livingroom) has zero classes under com/amazon/video/sdk,
// so the "flip the feature flag" approach has nothing to unlock here. Confirmed
// by disassembling all 4 ATV dex files vs. all 12 phone dex files.
//
// The ATV app instead owns its media3 ExoPlayer inside MediaPipelineBackendEngine
// and routes remote keys through IgniteRenderer$EventHandler.onKey before handing
// them to the native/WASM layer. These fingerprints target that path.
// ─────────────────────────────────────────────────────────────────────────────

// classes.dex — MediaPipelineBackendEngine.lambda$init$5(J)Ljava/lang/Integer;
// Builds the ExoPlayer via ExoPlayer$Builder and stores it in the `player` field.
// The log string "Initialising ExoPlayer" is unique to this method, so it anchors
// the match without depending on the synthetic lambda name (which can shift
// between builds).
object InitExoPlayerFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/livingroom/mediapipelinebackend/MediaPipelineBackendEngine;",
    filters = listOf(
        string("Initialising ExoPlayer")
    )
)

// classes.dex — MediaPipelineBackendEngine.releasePlayerInternal()V
// Calls player.release() then nulls the field. Anchored by "Releasing ExoPlayer".
object ReleaseExoPlayerFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/livingroom/mediapipelinebackend/MediaPipelineBackendEngine;",
    filters = listOf(
        string("Releasing ExoPlayer")
    )
)

// classes.dex — IgniteRenderer$EventHandler.onKey(View, int, KeyEvent)Z
// The View.OnKeyListener that forwards every remote key event into the native
// Ignite/WASM renderer. The single Java-side key chokepoint in the ATV app.
object OnKeyFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/ignitionshared/IgniteRenderer\$EventHandler;",
    name = "onKey",
    parameters = listOf(
        "Landroid/view/View;",
        "I",
        "Landroid/view/KeyEvent;"
    ),
    returnType = "Z"
)
