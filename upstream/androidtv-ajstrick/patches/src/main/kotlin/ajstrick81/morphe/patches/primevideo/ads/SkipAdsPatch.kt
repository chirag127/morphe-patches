package ajstrick81.morphe.patches.primevideo.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.primevideo.misc.extension.primeVideoExtensionPatch
import ajstrick81.morphe.patches.primevideo.shared.Constants

@Suppress("unused")
val skipAdsPatch = bytecodePatch(
    name = "Skip ads",
    description = "Multi-layer ad suppression targeting the SSAI schedule, impression reporting, and the Volley network chokepoint.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(primeVideoExtensionPatch)

    execute {

        // ─────────────────────────────────────────────────────────────────────
        // Hook 1 — media3 ServerSideAdInsertionMediaSource.setAdPlaybackStates()
        //
        // Strips all AdGroups from the incoming SSAI ad schedule before
        // ExoPlayer sees it. Primary suppression for standard SSAI path.
        // ─────────────────────────────────────────────────────────────────────
        SetAdPlaybackStatesMedia3Fingerprint.method.addInstructions(
            0,
            """
                invoke-static/range {p1 .. p1}, Lajstrick81/morphe/extension/primevideo/ads/SkipAdsPatch;->skipAllMedia3AdGroups(Lcom/google/common/collect/ImmutableMap;)Lcom/google/common/collect/ImmutableMap;
                move-result-object p1
            """
        )

        // ─────────────────────────────────────────────────────────────────────
        // Hook 2 — ExoPlayer2 ServerSideAdInsertionMediaSource.setAdPlaybackStates()
        //
        // Same strategy for the GMS Ads SDK ExoPlayer2 variant.
        // ─────────────────────────────────────────────────────────────────────
        SetAdPlaybackStatesExo2Fingerprint.method.addInstructions(
            0,
            """
                invoke-static/range {p1 .. p1}, Lajstrick81/morphe/extension/primevideo/ads/SkipAdsPatch;->skipAllExo2AdGroups(Lcom/google/common/collect/ImmutableMap;)Lcom/google/common/collect/ImmutableMap;
                move-result-object p1
            """
        )

        // ─────────────────────────────────────────────────────────────────────
        // Hook 3 — MetricsTransporter.transmit(SerializedBatch)
        //
        // Returns a fake SUCCESS UploadResult without making any network
        // request. Amazon's ad server receives no impression delivery data,
        // preventing it from accurately tracking ad viewing and reducing
        // scheduled ad load over time.
        //
        // Inline smali constructs UploadResult("SUCCESS", "ok") directly —
        // no extension class needed since UploadResult is in the app's own DEX.
        // ─────────────────────────────────────────────────────────────────────
        MetricsTransporterTransmitFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Lcom/amazon/minerva/client/thirdparty/transport/UploadResult;
                const-string v1, "SUCCESS"
                const-string v2, "ok"
                invoke-direct {v0, v1, v2}, Lcom/amazon/minerva/client/thirdparty/transport/UploadResult;-><init>(Ljava/lang/String;Ljava/lang/String;)V
                return-object v0
            """
        )

        // ─────────────────────────────────────────────────────────────────────
        // Hook 4 — Volley BasicNetwork.performRequest(Request)
        //
        // Inspects every Volley request's URL host before it leaves the
        // device. Known ad-decisioning / ad-tracking hosts are rejected with
        // a real NoConnectionError, which Volley's own RetryPolicy already
        // knows how to handle gracefully (same as a genuine connectivity
        // failure) — no faked contract, no hung pipeline.
        //
        // This is the control-plane equivalent of Hooks 1–3: it catches the
        // SSAI ad-decisioning call and any other Volley-routed ad traffic
        // that the AdPlaybackState/metrics hooks don't cover. It does NOT
        // suppress mid-roll ad segments — those are fetched by ExoPlayer's
        // own media3 DefaultHttpDataSource, which never touches Volley.
        // ─────────────────────────────────────────────────────────────────────
        BasicNetworkPerformRequestFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p1}, Lajstrick81/morphe/extension/primevideo/ads/SkipAdsPatch;->enforceAdBlock(Lcom/android/volley/Request;)V
            """
        )
    }
}
