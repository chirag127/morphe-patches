/*
 * MLB At Bat Android TV — Ad Suppression & Gambling-Ad Overlay Patch
 *
 * Validated against:
 *   v26.8.1.1 — com.bamnetworks.mobile.android.gameday.atbat
 *   (re-verified via androguard dex analysis of this exact build, 7 classesN.dex)
 *
 * Coverage:
 *   ✅ VOD ads              — createVodStreamRequest() empty zzdm →
 *                             IMA SDK throws → fallback to pre-cached CDN URL
 *   ✅ SSAI media source    — Lb6/h;.b0() blocked → no SSAI startup →
 *                             requestStream() never called → no DAI manifest URL
 *   ✅ DAI StreamManager    — Lb6/h;.m0() blocked → no ad segment scheduling
 *   ✅ TXXX dispatch        — Lu70/i;.onMetadata() blocked → no MLB EVI/IMA cues
 *   🧪 Commercial-break overlay — hooks onAdBreakStarted()/onAdBreakEnded()
 *                             to show/hide a full-screen overlay (Patch 5).
 *                             RE-ENABLED ALONGSIDE Patches 2/3/4 for field
 *                             testing — see note below.
 *
 * STATUS NOTE (this revision):
 *
 *   A prior revision disabled Patches 2/3/4 (blocking the SSAI session,
 *   DAI StreamManager, and TXXX metadata dispatch outright) in favor of
 *   Patch 5's overlay, reasoning that blocking SSAI was unconfirmed-safe
 *   for live games. A logcat capture was taken to validate this — but it
 *   was mistakenly captured against v1.4.107, a build that predates Patch
 *   5 entirely, so it only confirmed v1.4.107's pre-existing behavior
 *   (Patches 2/3/4 active, no overlay code present) and proved nothing
 *   about whether Patch 5 actually works.
 *
 *   Patches 2/3/4 are re-enabled here and Patch 5 is left active as well,
 *   so the next field test (logcat against THIS build) can determine:
 *     - whether Patches 2/3/4 cause any live-playback regressions, and
 *     - whether onAdBreakStarted()/onAdBreakEnded() ever fire once the
 *       SSAI session itself is blocked (they may not — Patch 2/3 prevent
 *       Lb6/h$g; construction, which is plausibly upstream of however
 *       those callbacks get invoked — but this needs to be confirmed by
 *       logcat rather than assumed).
 *   Update this note once results come back.
 */

package app.morphe.patches.mlbtv

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val atbatPatch = bytecodePatch(
    name = "MLB At Bat Android TV",
    description = "Removes VOD ads and shows a commercial-break overlay over live gambling ads " +
        "while preserving live game playback.",
) {
    compatibleWith(AppCompatibilities.MLB_TV)

    extendWith("extensions/extension.mpe")

    execute {
        // ------------------------------------------------------------------
        // Patch 1a: VOD SSAI — createVodStreamRequest (3-arg)
        // ------------------------------------------------------------------
        VodStreamRequest3ArgFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Lcom/google/ads/interactivemedia/v3/impl/zzdm;
                sget-object v1, Lcom/google/ads/interactivemedia/v3/internal/zzafs;->zzd:Lcom/google/ads/interactivemedia/v3/internal/zzafs;
                invoke-direct {v0, v1}, Lcom/google/ads/interactivemedia/v3/impl/zzdm;-><init>(Lcom/google/ads/interactivemedia/v3/internal/zzafs;)V
                return-object v0
            """.trimIndent(),
        )

        // ------------------------------------------------------------------
        // Patch 1b: VOD SSAI — createVodStreamRequest (4-arg)
        // ------------------------------------------------------------------
        VodStreamRequest4ArgFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Lcom/google/ads/interactivemedia/v3/impl/zzdm;
                sget-object v1, Lcom/google/ads/interactivemedia/v3/internal/zzafs;->zzd:Lcom/google/ads/interactivemedia/v3/internal/zzafs;
                invoke-direct {v0, v1}, Lcom/google/ads/interactivemedia/v3/impl/zzdm;-><init>(Lcom/google/ads/interactivemedia/v3/internal/zzafs;)V
                return-object v0
            """.trimIndent(),
        )

        // ------------------------------------------------------------------
        // Patch 2: SSAI MediaSource Startup — Lb6/h;.b0(Lq5/w;)V
        //
        // Verified: string="ImaServerSideAdInsertionMediaSource" (UNIQUE in APK)
        // proto=(Lq5/w;)V, registers=10
        //
        // Called when ImaServerSideAdInsertionMediaSource starts up.
        // return-void prevents: Lb6/h$g; construction → requestStream()
        // call → DAI manifest URL generation → dclk_video_ads segments.
        //
        // Re-enabled: field testing showed no live-playback regressions.
        // May also prevent onAdBreakStarted()/onAdBreakEnded() (Patch 5)
        // from firing — see top-of-file status note, to be confirmed by
        // the next logcat capture against this build.
        // ------------------------------------------------------------------
        SsaiMediaSourceStartupFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent(),
        )

        // ------------------------------------------------------------------
        // Patch 3: DAI StreamManager Event Handler — Lb6/h;.m0(StreamManager)V
        //
        // Verified: strings="IMA DAI Stream Event: ", "GSTREAM:DAI"
        // Belt-and-suspenders: prevents StreamManager from processing
        // DAI stream and scheduling ad segments even if Patch 2 is bypassed.
        //
        // Re-enabled for the same reason as Patch 2 — see top-of-file note.
        // ------------------------------------------------------------------
        DaiStreamManagerHandlerFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent(),
        )

        // ------------------------------------------------------------------
        // Patch 4: TXXX Metadata Dispatcher — Lu70/i;.onMetadata(Ll5/t;)V
        //
        // Blocks ALL HLS timed metadata dispatch:
        //   → Lz70/b;.o() never called → MLB EVI coroutines never launched
        //   → Lb6/h$c;.onMetadata() never called → IMA cues suppressed
        //
        // Re-enabled: this is also the path IMA's StreamManager uses
        // internally to detect ad-break boundaries, so it may interact
        // with Patch 5's onAdBreakStarted()/onAdBreakEnded() hooks — see
        // top-of-file status note.
        // ------------------------------------------------------------------
        ExoMediaPlayerMetadataFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent(),
        )

        // ------------------------------------------------------------------
        // Patch 5: Ad-Break Overlay
        //
        // 5a — registers the SSAI ad view group with the extension helper.
        // Lb6/h$d;.b(Ll5/q;)Lg6/w; calls
        //   Ll5/d;->getAdViewGroup()Landroid/view/ViewGroup;
        // immediately before passing the result into
        //   ImaSdkFactory.createStreamDisplayContainer(ViewGroup, VideoStreamPlayer)
        // The register holding the ViewGroup is located dynamically (rather
        // than hardcoded) since it can shift between app builds.
        // ------------------------------------------------------------------
        SsaiDisplayContainerFingerprint.method.apply {
            val instructions = implementation!!.instructions
            val getAdViewGroupIndex = instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.INVOKE_INTERFACE &&
                    ((instruction as ReferenceInstruction).reference as? MethodReference)?.name ==
                        "getAdViewGroup"
            }
            val moveResultIndex = getAdViewGroupIndex + 1
            val adViewGroupRegister =
                (instructions[moveResultIndex] as OneRegisterInstruction).registerA
            val totalRegisters = implementation!!.registerCount
            val paramRegisters = parameters.size + 1 // +1 for implicit `this` (p0)
            val firstParamRegister = totalRegisters - paramRegisters
            val registerName = if (adViewGroupRegister >= firstParamRegister) {
                "p${adViewGroupRegister - firstParamRegister}"
            } else {
                "v$adViewGroupRegister"
            }

            addInstructions(
                moveResultIndex + 1,
                """
                    invoke-static {$registerName}, Lajstrick81/morphe/extension/mlbtv/ads/AdBreakOverlayHelper;->registerAdViewGroup(Landroid/view/ViewGroup;)V
                """.trimIndent(),
            )
        }

        // 5b/5c — wire the no-op ad-break lifecycle callbacks to show/hide
        // the overlay. Both bodies are a single return-void (1 register,
        // `this`), so prepending at index 0 is unconditionally safe — no
        // registers are live yet for the verifier to conflict over.
        AdBreakStartedFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Lajstrick81/morphe/extension/mlbtv/ads/AdBreakOverlayHelper;->showOverlay()V
            """.trimIndent(),
        )

        AdBreakEndedFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Lajstrick81/morphe/extension/mlbtv/ads/AdBreakOverlayHelper;->hideOverlay()V
            """.trimIndent(),
        )
    }
}
