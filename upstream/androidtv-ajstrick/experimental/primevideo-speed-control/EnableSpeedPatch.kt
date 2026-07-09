package ajstrick81.morphe.patches.primevideo.speed

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.primevideo.misc.extension.primeVideoExtensionPatch
import ajstrick81.morphe.patches.primevideo.shared.Constants
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS =
    "Lajstrick81/morphe/extension/primevideo/speed/SpeedControlPatch;"

@Suppress("unused")
val playbackSpeedPatch = bytecodePatch(
    name = "Enable speed control",
    description = "Experimental playback-speed control for the ATV player. " +
        "Remote fast-forward cycles speed up (1x→1.5x→2x→4x→8x→16x), rewind " +
        "cycles down. Useful for blowing through ad segments that survive the " +
        "SkipAds and DNS layers (media3 never gated setPlaybackSpeed on ad state).",
) {
    compatibleWith(Constants.COMPATIBILITY)

    // Pulls in the compiled extension dex that carries SpeedControlPatch.
    dependsOn(primeVideoExtensionPatch)

    execute {

        // ─────────────────────────────────────────────────────────────────────
        // Hook 1 — capture the ExoPlayer instance.
        //
        // MediaPipelineBackendEngine.lambda$init$5 builds the player with
        // ExoPlayer$Builder and stores it:
        //     iput-object vX, vThis, ...->player Landroidx/media3/exoplayer/ExoPlayer;
        // We inject setCurrentPlayer(vX) immediately after that store, while the
        // freshly-built player is still in register vX (the app reuses it right
        // after, so the insertion point matters). The register is located
        // dynamically since it can shift between builds.
        // ─────────────────────────────────────────────────────────────────────
        InitExoPlayerFingerprint.method.apply {
            val instructions = implementation!!.instructions
            val putIndex = instructions.indexOfFirst { instruction ->
                instruction.opcode == Opcode.IPUT_OBJECT &&
                    ((instruction as ReferenceInstruction).reference as? FieldReference)?.name == "player"
            }
            // registerA of the iput-object is the source value — the freshly
            // built player. It is a reused local here (< 16), and our injected
            // invoke-static allocates no new registers, so the raw vN number is
            // stable and unambiguous.
            val playerRegister =
                (instructions[putIndex] as TwoRegisterInstruction).registerA

            addInstructions(
                putIndex + 1,
                "invoke-static {v$playerRegister}, " +
                    "$EXTENSION_CLASS->setCurrentPlayer(Landroidx/media3/common/Player;)V",
            )
        }

        // ─────────────────────────────────────────────────────────────────────
        // Hook 2 — drop the reference when the player is released.
        //
        // releasePlayerInternal() calls player.release() then nulls the field.
        // Clearing our static holder at entry prevents a late key press from
        // touching a released player. index 0 is safe: no registers are live.
        // ─────────────────────────────────────────────────────────────────────
        ReleaseExoPlayerFingerprint.method.addInstructions(
            0,
            "invoke-static {}, $EXTENSION_CLASS->clearCurrentPlayer()V",
        )

        // ─────────────────────────────────────────────────────────────────────
        // Hook 3 — intercept remote keys before the WASM layer.
        //
        // IgniteRenderer$EventHandler.onKey(View p1, int p2 keyCode, KeyEvent p3)
        // forwards every key into native Ignite. We call onKeyEvent(keyCode,
        // event) first; if it returns true we consumed the key (a speed change)
        // and return true immediately so the app never seeks. Otherwise we fall
        // through to the original body untouched.
        // ─────────────────────────────────────────────────────────────────────
        OnKeyFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p2, p3}, $EXTENSION_CLASS->onKeyEvent(ILandroid/view/KeyEvent;)Z
                move-result v0
                if-eqz v0, :speed_not_consumed
                const/4 v0, 0x1
                return v0
                :speed_not_consumed
                nop
            """,
        )
    }
}
