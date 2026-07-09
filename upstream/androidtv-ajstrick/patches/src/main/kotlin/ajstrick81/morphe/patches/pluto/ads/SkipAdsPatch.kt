package ajstrick81.morphe.patches.pluto.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.pluto.shared.Constants

@Suppress("unused")
val skipAdsPatch = bytecodePatch(
    name = "Skip ads",
    description = "Suppresses Pluto TV's ads. Empties the client-side ad-break timeline " +
        "(StitcherSession.adBreaks) — the same data AdGuard strips via jsonprune — which " +
        "removes on-demand (VOD) ad breaks entirely: ad video, markers, overlays, and " +
        "beacons. Also no-ops pause ads and clickable-ad overlays. LIVE TV ads are real " +
        "broadcast time in the linear feed and are not removable. Validated on-device, " +
        "5.66.0-leanback.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {

        // Hook 1 — BeaconTracker.fire(String, List)
        // Silences every stitched-ad tracking beacon (impressions, quartiles,
        // completes). Fire-and-forget void method — no playback dependency.
        BeaconTrackerFireFingerprint.method.addInstructions(0, "return-void")

        // Hook 2 — PauseAdsImageBinder.showPauseAdImageAfterInactivity(Bitmap, List)
        // Suppresses the pause-ad overlay and its beacons.
        PauseAdsShowFingerprint.method.addInstructions(0, "return-void")

        // Hook 3 — ClickableAdsBinder$Companion.bind(...): Disposable
        // Prevents clickable ("interactive") ad overlays from ever being wired
        // by returning an already-disposed Disposable at offset 0. The caller
        // adds it to a CompositeDisposable as usual — a disposed Disposable is
        // a harmless no-op, so the contract is honored and nothing hangs.
        //
        // Verifier note: at offset 0 no registers are live, so writing a fresh
        // Disposable into v0 and returning it is always accepted (same pattern
        // as the Peacock getOkHttpClient replacement). Disposables.disposed()
        // is confirmed present in the app's bundled RxJava.
        ClickableAdsBindFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Lio/reactivex/disposables/Disposables;->disposed()Lio/reactivex/disposables/Disposable;
                move-result-object v0
                return-object v0
            """,
        )

        // Hook 4 — StitcherSession.getAdBreaks() -> empty list
        // Reproduces AdGuard Premium's proven suppression in bytecode: its
        // filtering log strips the ad-break timeline from the stitcher session
        // response (||pluto.tv/*/session.json$jsonprune=$.adBreaks.*). This is
        // the in-app equivalent at the single client-side model choke point —
        // every consumer of the parsed StitcherSession (timebar
        // AdBreakPositionSource, the ID3/slot beacon controllers, and the
        // player's ad-break handling) reads the breaks through this getter, so
        // returning an empty list de-schedules the entire ad experience.
        // getAdBreaks() has .locals 1, so v0 is valid at entry.
        StitcherSessionGetAdBreaksFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;
                move-result-object v0
                return-object v0
            """,
        )
    }
}
