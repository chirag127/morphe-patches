package ajstrick81.morphe.patches.primevideo.ads

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

// ─────────────────────────────────────────────────────────────────────────────
// Primary target — media3 SSAI ad schedule entry point
// classes.dex / smali/androidx/media3/exoplayer/source/ads/
// ─────────────────────────────────────────────────────────────────────────────
object SetAdPlaybackStatesMedia3Fingerprint : Fingerprint(
    definingClass = "Landroidx/media3/exoplayer/source/ads/ServerSideAdInsertionMediaSource;",
    name = "setAdPlaybackStates",
    parameters = listOf(
        "Lcom/google/common/collect/ImmutableMap;",
        "Landroidx/media3/common/Timeline;"
    ),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC)
)

// ─────────────────────────────────────────────────────────────────────────────
// Secondary target — ExoPlayer2 SSAI ad schedule entry point
// classes3.dex / smali/com/google/android/exoplayer2/source/ads/
// ─────────────────────────────────────────────────────────────────────────────
object SetAdPlaybackStatesExo2Fingerprint : Fingerprint(
    definingClass = "Lcom/google/android/exoplayer2/source/ads/ServerSideAdInsertionMediaSource;",
    name = "setAdPlaybackStates",
    parameters = listOf(
        "Lcom/google/common/collect/ImmutableMap;"
    ),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC)
)

// ─────────────────────────────────────────────────────────────────────────────
// Tertiary target — MetricsTransporter.transmit(SerializedBatch)
// classes2.dex / smali/com/amazon/minerva/client/thirdparty/transport/
//
// The Java-layer impression reporting pipeline that successfully uploads
// ad metrics to Amazon's servers (HTTP 200 OK, 224 uploads in a single
// ad session). Confirmed in logcat: "Successfully uploaded metrics; code: 200"
//
// Returning a fake SUCCESS UploadResult prevents Amazon from receiving
// impression delivery reports — without impression data, Amazon's ad server
// cannot accurately track whether ads are being viewed, which should reduce
// ad scheduling pressure over time (the impression deficit effect).
//
// Strategy: construct a fake UploadResult(SUCCESS, "ok") and return it
// without making any network request. The caller sees a successful upload
// and moves on normally.
//
// UploadResult constructor: <init>(String uploadStatus, String uploadMessage)
// SUCCESS constant: UploadResult.SUCCESS = "SUCCESS"
//
// This is the deception-over-brute-force approach applied to the metrics
// layer — Amazon's ad server thinks impressions are being reported normally.
// ─────────────────────────────────────────────────────────────────────────────
object MetricsTransporterTransmitFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/minerva/client/thirdparty/transport/MetricsTransporter;",
    name = "transmit",
    parameters = listOf(
        "Lcom/amazon/minerva/client/thirdparty/transport/SerializedBatch;"
    ),
    returnType = "Lcom/amazon/minerva/client/thirdparty/transport/UploadResult;",
    accessFlags = listOf(AccessFlags.PUBLIC)
)

// ─────────────────────────────────────────────────────────────────────────────
// Hook 5 target — Volley network-layer chokepoint
// classes2.dex / smali/com/android/volley/toolbox/
//
// Prime Video has NO OkHttp anywhere in its dex set (confirmed via exhaustive
// search) — its app-layer HTTP client is Volley, wired up by Amazon's own
// VolleyModule (com.amazon.ignitionshared.network.VolleyModule), which
// constructs a HurlStack (HttpURLConnection-based) wrapped in BasicNetwork.
//
// BasicNetwork is the ONLY class implementing com.android.volley.Network in
// the entire app (the library's AsyncNetwork is unused) and is directly
// instantiated by VolleyModule with no app-specific subclass — confirmed via
// disassembly of VolleyModule's Dagger factory methods. This makes
// performRequest() the single, unavoidable chokepoint for every Volley
// request: catalog/metadata APIs, the SSAI ad-decisioning ("sgai"/"draper")
// call, and impression/metrics reporting.
//
// Distinct from Hooks 1–3: this is the control-plane HTTP layer, not the
// media-plane data source. ExoPlayer/media3 fetches actual segments (incl.
// mid-roll ad segments multiplexed onto the content CDN host) via its own
// androidx.media3.datasource.DefaultHttpDataSource, which never touches
// Volley — so this hook cannot suppress mid-roll ad segments, only requests
// that go through Volley (ad decisioning + metrics + most app APIs).
// ─────────────────────────────────────────────────────────────────────────────
object BasicNetworkPerformRequestFingerprint : Fingerprint(
    definingClass = "Lcom/android/volley/toolbox/BasicNetwork;",
    name = "performRequest",
    parameters = listOf(
        "Lcom/android/volley/Request;"
    ),
    returnType = "Lcom/android/volley/NetworkResponse;",
    accessFlags = listOf(AccessFlags.PUBLIC)
)
