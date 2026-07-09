package ajstrick81.morphe.patches.foxone.ads

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

// ─────────────────────────────────────────────────────────────────────────────
// Fox One (com.fox.foxone) — Ad suppression fingerprints
//
// Binary analysis confirmed on APK pulled from Onn 4K TV (version pulled
// after Fox Sports discontinuation — Fox One is now the primary Fox app).
//
// ARCHITECTURE:
//   - Fully native (no DWebView/DSBridge WebView hybrid)
//   - ExoPlayer (not Bitmovin)
//   - Two ad pipelines:
//       VOD  → Google IMA/DAI via FoxImaAdListeners + FoxImaStreamIdLoader
//       Live → Yospace SSAI via YospaceAnalyticEventObserver
//   - AdRise integration for CSAI token auth (adRiseToken/adRiseExpiration)
//     matching AGP ruleset finding: "Tubi/AdRise — CSAI decision engine"
//   - CDN: foxdtc-video.akamaized.net (Akamai) on Onn TV for live streams
//
// LAMBDA NUMBER CHANGE vs v1.9.2:
//   Hook 1: lambda$10$lambda$9 → lambda$0$0  (verified: contains "adEvent")
//   Hook 2: lambda$4$lambda$3  → lambda$0$0  (verified: contains "onAdsManagerLoaded")
//   Hooks 3–8: unchanged, all verified ✅
//
// For HLS segment layer suppression on the Onn TV, use the AGP v4.6 ruleset
// targeting foxdtc-video.akamaized.net (Akamai CDN path for Android TV).
// ─────────────────────────────────────────────────────────────────────────────

// Hook 1 — FoxImaAdListeners.adEventListener_delegate$lambda$0$0
//
// Inner nested lambda that receives every Google IMA ad event
// (AD_BREAK_STARTED, AD_STARTED, AD_PROGRESS, AD_COMPLETED, etc.)
// before dispatching to FoxPlayer. Confirmed via "adEvent" string in body.
// Lambda numbering changed from lambda$10$lambda$9 in v1.9.2.
object FoxImaAdEventListenerFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/api/ima/listeners/FoxImaAdListeners;",
    name = "adEventListener_delegate\$lambda\$0\$0",
    strings = listOf("adEvent"),
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC, AccessFlags.FINAL)
)

// Hook 2 — FoxImaAdListeners.adsLoadedListener_delegate$lambda$0$0
//
// Inner nested lambda that fires when the IMA AdsManager finishes loading.
// Only place that calls imaStreamManager.init() to prime the ad timeline.
// Confirmed via "onAdsManagerLoaded" and stream ID strings in body.
// Lambda numbering changed from lambda$4$lambda$3 in v1.9.2.
object FoxImaAdsLoadedListenerFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/api/ima/listeners/FoxImaAdListeners;",
    name = "adsLoadedListener_delegate\$lambda\$0\$0",
    strings = listOf("onAdsManagerLoaded"),
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC, AccessFlags.FINAL)
)

// Hook 3 — FoxPlayer.clearVodAds()
// Verified present in classes.dex. No access flags constraint (none needed —
// method name is unique within FoxPlayer).
object FoxPlayerClearVodAdsFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/FoxPlayer;",
    name = "clearVodAds"
)

// Hook 4 — FoxImaStreamIdLoader.requestVODDAIUrl
// Verified in classes6.dex with exact parameter types.
object FoxImaVodStreamRequestFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/api/ima/loaders/FoxImaStreamIdLoader;",
    name = "requestVODDAIUrl",
    strings = listOf("requestVODDAIUrl() BEGIN..."),
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/lang/Boolean;",
        "Lcom/fox/android/video/player/loaders/ImaStreamIdLoader\$ImaStreamUrlCallback;"
    ),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC)
)

// Hook 5 — FoxImaStreamIdLoader.requestImaStreamId
// Verified in classes6.dex with exact parameter types.
object FoxImaLiveStreamRequestFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/api/ima/loaders/FoxImaStreamIdLoader;",
    name = "requestImaStreamId",
    strings = listOf("requestImaStreamId BEGIN..."),
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/lang/Boolean;",
        "Lcom/fox/android/video/player/loaders/ImaStreamIdLoader\$ImaStreamIdCallback;"
    ),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC)
)

// Hook 6 — YospaceAnalyticEventObserver.dispatchAdEvent(FoxAdEvent)
// LIVE CONTENT. Verified in classes.dex. Parameters unchanged.
object YospaceDispatchAdEventFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/yospace/listener/YospaceAnalyticEventObserver;",
    name = "dispatchAdEvent",
    parameters = listOf("Lcom/fox/android/video/player/event/FoxAdEvent;"),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL)
)

// Hook 7 — YospaceAnalyticEventObserver.dispatchSlateEvent(FoxSlateEvent)
// LIVE CONTENT. Verified in classes.dex. Parameters unchanged.
object YospaceDispatchSlateEventFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/yospace/listener/YospaceAnalyticEventObserver;",
    name = "dispatchSlateEvent",
    parameters = listOf("Lcom/fox/android/video/player/event/FoxSlateEvent;"),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL)
)

// Hook 8 — YospaceSeekablePlaybackPolicyHandler.setHandleFastForwardSeek
// LIVE CONTENT. Verified in classes.dex. Seek policy enforcement is a
// separate code path from event dispatch — required alongside Hooks 6–7.
object YospaceSeekPolicyFingerprint : Fingerprint(
    definingClass = "Lcom/fox/android/video/player/yospace/handler/YospaceSeekablePlaybackPolicyHandler;",
    name = "setHandleFastForwardSeek",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL)
)
