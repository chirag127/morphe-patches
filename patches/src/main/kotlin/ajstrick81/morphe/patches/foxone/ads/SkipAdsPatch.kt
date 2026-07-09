package ajstrick81.morphe.patches.foxone.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.foxone.shared.Constants

@Suppress("unused")
val foxOneSkipAdsPatch = bytecodePatch(
    name = "Skip ads",
    description = "Suppresses all ad delivery systems in Fox One Android TV: " +
        "Google IMA/DAI for VOD, Yospace SSAI for live content.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {

        // Hook 1 — FoxImaAdListeners.adEventListener_delegate$lambda$0$0
        // Lambda numbering changed from lambda$10$lambda$9 in v1.9.2 to
        // lambda$0$0 in this version. Verified: contains "adEvent" string.
        FoxImaAdEventListenerFingerprint.method.addInstructions(0, "return-void")

        // Hook 2 — FoxImaAdListeners.adsLoadedListener_delegate$lambda$0$0
        // Lambda numbering changed from lambda$4$lambda$3 in v1.9.2 to
        // lambda$0$0 in this version. Verified: contains "onAdsManagerLoaded".
        FoxImaAdsLoadedListenerFingerprint.method.addInstructions(0, "return-void")

        // Hook 3 — FoxPlayer.clearVodAds()
        FoxPlayerClearVodAdsFingerprint.method.addInstructions(0, "return-void")

        // Hook 4 — FoxImaStreamIdLoader.requestVODDAIUrl
        FoxImaVodStreamRequestFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "dai_blocked"
                invoke-interface {p3, v0}, Lcom/fox/android/video/player/loaders/ImaStreamIdLoader${"$"}ImaStreamUrlCallback;->onFailure(Ljava/lang/String;)V
                return-void
            """
        )

        // Hook 5 — FoxImaStreamIdLoader.requestImaStreamId
        FoxImaLiveStreamRequestFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "dai_blocked"
                invoke-interface {p3, v0}, Lcom/fox/android/video/player/loaders/ImaStreamIdLoader${"$"}ImaStreamIdCallback;->onFailure(Ljava/lang/String;)V
                return-void
            """
        )

        // Hook 6 — YospaceAnalyticEventObserver.dispatchAdEvent — LIVE CONTENT
        YospaceDispatchAdEventFingerprint.method.addInstructions(0, "return-void")

        // Hook 7 — YospaceAnalyticEventObserver.dispatchSlateEvent — LIVE CONTENT
        YospaceDispatchSlateEventFingerprint.method.addInstructions(0, "return-void")

        // Hook 8 — YospaceSeekablePlaybackPolicyHandler.setHandleFastForwardSeek
        YospaceSeekPolicyFingerprint.method.addInstructions(0, "return-void")
    }
}
