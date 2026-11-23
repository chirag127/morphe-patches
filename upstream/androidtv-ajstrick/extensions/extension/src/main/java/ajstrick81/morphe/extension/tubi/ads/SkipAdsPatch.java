package ajstrick81.morphe.extension.tubi.ads;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

/**
 * Tubi — FoxPlayer/FoxIMA + WebView ad suppression extension.
 *
 * onClearVodAds() — Hook 3: FoxPlayer.clearVodAds() amplification.
 *   Nulls out the IMA StreamManager reference in FoxImaAdListeners via
 *   reflection to prevent stale IMA sessions from reactivating after clear.
 *   FoxPlayer is passed as Object to avoid needing a stub class.
 *
 * shouldBlock() — Hook 7: WebViewClient.shouldInterceptRequest() interception.
 *   Called for every network request the TubiWebView SPA makes. Returns an
 *   empty WebResourceResponse for ad domains (blocking the request), or null
 *   (pass-through) for all other URLs.
 *
 *   WHY THIS EXISTS:
 *   Tubi is a hybrid app. The SPA at https://ott-androidtv.tubitv.com runs
 *   inside TubiWebView (wrapping DWebView/DSBridge) and drives the ENTIRE
 *   pre-roll ad lifecycle in JavaScript. ExoPlayer only initialises AFTER the
 *   pre-roll ends, when the SPA calls the JS→Native bridge startNativePlayer().
 *   Hooks 1–6 (all in Java/Kotlin classes) have zero effect on JS-driven ads.
 *   shouldInterceptRequest() is the only native interception point that reaches
 *   the WebView's network layer.
 *
 *   The WebView SPA architecture is also used in Fox Sports and Fox One —
 *   this pattern explains why pre-rolls survived all previous native patches
 *   and AGP DNS rules in those apps. Applying the same hook to their
 *   WebViewClient subclasses should eliminate those pre-rolls too.
 *
 *   ANDROID SDK IMPORTS:
 *   android.webkit.WebResourceRequest and android.webkit.WebResourceResponse
 *   cannot be imported in Morphe extensions (compilation environment lacks
 *   full Android SDK). All Android types are accessed via reflection and
 *   Class.forName() so the extension compiles against standard Java only.
 *
 *   MORPHE CONSTRAINT NOTE:
 *   addInstructions() does NOT support labels or conditional branches in
 *   injected smali. Hook 7 uses straight-line code:
 *     invoke-static {p2}, shouldBlock(Object)Object
 *     move-result-object v0
 *     return-object v0
 *   This works because shouldInterceptRequest()'s null contract means
 *   returning null = pass-through and returning a WebResourceResponse = block.
 *
 * All operations are wrapped in try/catch — any failure is a silent no-op
 * so playback continues normally.
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class SkipAdsPatch {

    // ─────────────────────────────────────────────────────────────────────────
    // Ad domain blocklist for Hook 7 (shouldBlock)
    //
    // Confirmed via AGP DNS block test: blocking dai.google.com eliminates
    // the WebView-driven pre-roll entirely. The remaining domains cover the
    // full IMA/DAI delivery stack used by the SPA's JavaScript ad engine.
    // ─────────────────────────────────────────────────────────────────────────
    private static final String[] BLOCKED_HOSTS = {
        "dai.google.com",
        "imasdk.googleapis.com",
        "googletagmanager.com",
        "doubleclick.net",
        "googlesyndication.com"
    };

    /**
     * Hook 3 — Called at index 0 of FoxPlayer.clearVodAds().
     *
     * Nulls out the IMA StreamManager reference in FoxImaAdListeners via
     * reflection, preventing any stale IMA session from reactivating ad
     * delivery after clearVodAds() is called during content transitions.
     *
     * @param foxPlayer The FoxPlayer instance (typed as Object to avoid stub).
     */
    public static void onClearVodAds(Object foxPlayer) {
        if (foxPlayer == null) return;
        try {
            java.lang.reflect.Field adsLoaderField =
                foxPlayer.getClass().getDeclaredField("imaAdsLoader");
            adsLoaderField.setAccessible(true);
            Object adsLoader = adsLoaderField.get(foxPlayer);
            if (adsLoader == null) return;

            java.lang.reflect.Field listenersField =
                adsLoader.getClass().getDeclaredField("imaAdListeners");
            listenersField.setAccessible(true);
            Object listeners = listenersField.get(adsLoader);
            if (listeners == null) return;

            java.lang.reflect.Field managerField =
                listeners.getClass().getDeclaredField("imaStreamManager");
            managerField.setAccessible(true);
            managerField.set(listeners, null);

        } catch (Exception e) {
            // Silent fail — Hook 1 return-void is the primary suppression
        }
    }

    /**
     * Hook 7 — Called at index 0 of xo/C$c.shouldInterceptRequest().
     *
     * Checks the incoming WebResourceRequest URL host against the ad domain
     * blocklist. Returns an empty WebResourceResponse for blocked domains,
     * or null for all other URLs (pass-through to normal Android handling).
     *
     * All Android SDK types (WebResourceRequest, WebResourceResponse, Uri)
     * are accessed via reflection to avoid import dependencies on the Android
     * SDK in the extension compilation environment.
     *
     * @param request The android.webkit.WebResourceRequest (typed as Object).
     * @return        Empty WebResourceResponse for ad domains, null otherwise.
     */
    public static Object shouldBlock(Object request) {
        if (request == null) return null;
        try {
            // WebResourceRequest.getUrl() → android.net.Uri
            Method getUrl = request.getClass().getMethod("getUrl");
            Object uri = getUrl.invoke(request);
            if (uri == null) return null;

            // Uri.getHost() → String
            Method getHost = uri.getClass().getMethod("getHost");
            Object hostObj = getHost.invoke(uri);
            if (hostObj == null) return null;
            String host = hostObj.toString();

            // Check against blocklist
            for (String blocked : BLOCKED_HOSTS) {
                if (host.equals(blocked) || host.endsWith("." + blocked)) {
                    // Construct WebResourceResponse via reflection:
                    // new WebResourceResponse("text/plain", "utf-8", emptyStream)
                    Class<?> wrrClass = Class.forName("android.webkit.WebResourceResponse");
                    java.lang.reflect.Constructor<?> ctor = wrrClass.getConstructor(
                        String.class, String.class, java.io.InputStream.class
                    );
                    return ctor.newInstance(
                        "text/plain",
                        "utf-8",
                        new ByteArrayInputStream(new byte[0])
                    );
                }
            }
        } catch (Exception e) {
            // Silent fail — return null (pass-through) on any reflection error
        }
        return null;
    }
}
