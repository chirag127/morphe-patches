package ajstrick81.morphe.extension.primevideo.ads;

import android.util.Log;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Prime Video ATV — ad suppression extension.
 *
 * Entry points called from patched bytecode:
 *
 *   skipAllMedia3AdGroups — strips ad schedule from media3 AdPlaybackState map
 *   skipAllExo2AdGroups   — same for ExoPlayer2 / GMS Ads variant
 *   enforceAdBlock        — Volley BasicNetwork.performRequest() host filter
 *
 * Note: Hook 3 (MetricsTransporter.transmit) uses pure inline smali to
 * construct a fake UploadResult("SUCCESS", "ok") directly — no Java extension
 * method needed since UploadResult lives in the app's own DEX.
 *
 * --- AdPlaybackState suppression (Hooks 1 & 2) ---
 *
 * withRemovedAdGroupCount(adGroupCount) physically removes all ad groups
 * from the AdPlaybackState before ExoPlayer sees the map. Operates at the
 * SSAI schedule layer — primary suppression for standard ad delivery.
 *
 * Confirmed via dex disassembly that there is no separate Java-level
 * pipeline for the WASM-rendered Ignite UI layer (pause overlays, promo
 * panels, etc.) — that UI is driven entirely by Amazon's native/WASM
 * IgniteRenderer via an opaque message bus (GMBMessageProcessor), with no
 * discrete interceptable class. Any ad content rendered through that path
 * is out of scope for bytecode hooking.
 *
 * --- Volley network-layer filtering (Hook 4) ---
 *
 * Prime Video has zero OkHttp classes anywhere in its dex set; its
 * app-layer HTTP client is Volley, wired by Amazon's own VolleyModule
 * (com.amazon.ignitionshared.network.VolleyModule) through a HurlStack
 * wrapped in BasicNetwork — the sole implementation of Volley's Network
 * interface in the app, with no app-specific subclass. enforceAdBlock()
 * runs at the top of BasicNetwork.performRequest(Request) and rejects
 * known ad-decisioning/ad-tracking hosts, plus the getVideoAds path on
 * the dual-use atv-ps.amazon.com playback host, before any HTTP work
 * happens.
 *
 * Throwing a real NoConnectionError (rather than faking a successful
 * response) matters: Volley's own RetryPolicy/NetworkDispatcher already
 * knows how to handle this exception type for genuine connectivity
 * failures, so the request fails the same way it would on a real network
 * error — no faked contract, no risk of hanging a caller that expects a
 * specific response shape.
 *
 * This hook does NOT suppress mid-roll ad segments — those are fetched by
 * Amazon's native MediaPipelineBackend (libcurl), a completely separate HTTP
 * stack that never touches Volley. Confirmed via on-device logcat: the
 * DOWNLOADER tag (native) fetches manifests/segments directly, including ad
 * segments delivered at an /iad_X/ path (where X is a segment ID) on the SAME safe-harbored content
 * CDN host as the movie itself - a path-only distinction invisible to both
 * this hook and DNS blocking. No bytecode-reachable mitigation exists for
 * that delivery mode on this build.
 *
 * All methods wrapped in try/catch - failures are logged to TAG below instead
 * of silently swallowed, so logcat can confirm whether the hook fired and
 * whether the strip actually took effect.
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class SkipAdsPatch {

    private static final String TAG = "SkipAdsPatch";

    // ── AdPlaybackState suppression ───────────────────────────────────────────

    /**
     * Transforms an AdPlaybackState map for the media3 SSAI pipeline.
     *
     * Called at index 0 of:
     *   androidx.media3.exoplayer.source.ads.ServerSideAdInsertionMediaSource
     *       .setAdPlaybackStates(ImmutableMap, Timeline)
     */
    public static ImmutableMap skipAllMedia3AdGroups(ImmutableMap adPlaybackStates) {
        try {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            int strippedGroups = 0;
            for (Object o : adPlaybackStates.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object key = entry.getKey();
                androidx.media3.common.AdPlaybackState state =
                        (androidx.media3.common.AdPlaybackState) entry.getValue();
                if (state.adGroupCount > state.removedAdGroupCount) {
                    strippedGroups += state.adGroupCount - state.removedAdGroupCount;
                    state = state.withRemovedAdGroupCount(state.adGroupCount);
                }
                builder.put(key, state);
            }
            Log.i(TAG, "skipAllMedia3AdGroups: entries=" + adPlaybackStates.size()
                    + " strippedGroups=" + strippedGroups);
            return builder.build();
        } catch (Exception e) {
            Log.e(TAG, "skipAllMedia3AdGroups failed", e);
            return adPlaybackStates;
        }
    }

    /**
     * Transforms an AdPlaybackState map for the ExoPlayer2 SSAI pipeline
     * bundled inside the Google Mobile Ads SDK (classes4.dex).
     *
     * Called at index 0 of:
     *   com.google.android.exoplayer2.source.ads.ServerSideAdInsertionMediaSource
     *       .setAdPlaybackStates(ImmutableMap)
     */
    public static ImmutableMap skipAllExo2AdGroups(ImmutableMap adPlaybackStates) {
        try {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            int strippedGroups = 0;
            for (Object o : adPlaybackStates.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object key = entry.getKey();
                com.google.android.exoplayer2.source.ads.AdPlaybackState state =
                        (com.google.android.exoplayer2.source.ads.AdPlaybackState) entry.getValue();
                if (state.adGroupCount > state.removedAdGroupCount) {
                    strippedGroups += state.adGroupCount - state.removedAdGroupCount;
                    state = state.withRemovedAdGroupCount(state.adGroupCount);
                }
                builder.put(key, state);
            }
            Log.i(TAG, "skipAllExo2AdGroups: entries=" + adPlaybackStates.size()
                    + " strippedGroups=" + strippedGroups);
            return builder.build();
        } catch (Exception e) {
            Log.e(TAG, "skipAllExo2AdGroups failed", e);
            return adPlaybackStates;
        }
    }

    // ── Volley network-layer filtering ────────────────────────────────────────

    /**
     * Called at index 0 of:
     *   com.android.volley.toolbox.BasicNetwork.performRequest(Request)
     *
     * Rejects requests to known ad-decisioning/ad-tracking hosts before any
     * HTTP work happens, by throwing a real NoConnectionError — the same
     * exception type Volley already raises for genuine connectivity
     * failures, so its RetryPolicy/NetworkDispatcher handle it exactly as
     * they would a real network outage.
     */
    public static void enforceAdBlock(Request<?> request) throws NoConnectionError {
        try {
            String url = request.getUrl();
            if (url == null) return;

            // Ad decisioning is a PATH on the dual-use playback host
            // atv-ps.amazon.com, which also serves Widevine licensing and
            // session/playback APIs — never block the host itself, only this
            // path. Confirmed via PC capture: getVideoAds is the exact call
            // gating preroll ad markers; an external filter blocking it at
            // the DNS level (without a path-scoped client-side fallback)
            // produced a 5-10s player stall instead of a clean skip, since
            // the app's own retry/backoff still waited on a response that
            // could never arrive. Throwing NoConnectionError here is the
            // same fail-fast contract as the host-based blocks below, so the
            // app's ad-loading state machine resolves immediately instead
            // of timing out.
            if (url.contains("/cdp/getVideoAds")) {
                Log.i(TAG, "enforceAdBlock: blocking getVideoAds (ad decisioning)");
                throw new NoConnectionError(new IOException("ads_blocked: getVideoAds"));
            }

            String host = new URI(url).getHost();
            if (host == null) return;
            host = host.toLowerCase();

            // Allowlist-by-default: only the hosts matched below are rejected;
            // everything else passes through untouched. In particular the §1
            // safe-harbor hosts are never matched here — the api.amazonvideo.com
            // patterns are anchored to the threeplr*/nit* ad prefixes so they
            // cannot collide with keho/qgmg/p7kg/abxc3apcastp, and the weblab
            // rule is an exact match for the single zoar ad-trigger host so the
            // rest of *.weblab.a2z.com is left alone.
            //
            // Host inventory derived from an ad-free vs ad-supported session
            // diff (Prime Video filter list v3.6). These are the control-plane
            // (Volley-routed) ad hosts only — the media-plane ad segment CDNs
            // (*.pv-cdn.net / *.akamaihd.net) are fetched by media3's
            // DefaultHttpDataSource and are out of scope for this hook.
            boolean blocked =
                    // Ad exchange / decisioning (covers s. and mads. subdomains)
                    host.equals("amazon-adsystem.com")
                    || host.endsWith(".amazon-adsystem.com")
                    // Ad delivery network — broadened from the old ters-* regex
                    // to every aiv-delivery.net subdomain per the v3.6 list.
                    || host.equals("aiv-delivery.net")
                    || host.endsWith(".aiv-delivery.net")
                    // Weblab ad trigger — confirmed ad-specific (absent in
                    // ad-free sessions). Exact match keeps the rest of weblab
                    // in the safe harbor. Prime suspect for persisting prerolls.
                    || host.equals("zoar.triggers-v1.prod.mobile.weblab.a2z.com")
                    || host.equals("hercule.triggers-v1.prod.mobile.weblab.a2z.com")
                    // Ad orchestration endpoints on the video API.
                    || host.matches("threeplr[a-z0-9.-]*\\.api\\.amazonvideo\\.com")
                    || host.matches("nit[a-z0-9.-]*\\.api\\.amazonvideo\\.com")
                    // s0s7: fires every ad cycle per on-device DNS log; never seen
                    // in any PC/web capture, so its transport layer (Volley vs.
                    // native libcurl) on this app is unconfirmed. Mirrored here as
                    // a no-cost safety net for patch-only users without AGH — if
                    // it's native-only this branch simply never fires.
                    || host.equals("s0s7.api.amazonvideo.com")
                    // Pause-screen ad endpoint surfaced via GetVodPlaybackResources
                    // -> pauseAdsResolutionUrl (/getAds?...&format=PAUSE_ADS_STATIC).
                    || host.equals("regolith.prime-video.amazon.dev");

            if (blocked) {
                Log.i(TAG, "enforceAdBlock: blocking " + host);
                throw new NoConnectionError(new IOException("ads_blocked: " + host));
            }
        } catch (URISyntaxException e) {
            // malformed URL — let it through, not our concern
        }
    }

}
