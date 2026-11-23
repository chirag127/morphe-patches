package hoodles.morphe.patches.primevideo.speed

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.patches.primevideo.shared.Constants

@Suppress("unused")
val playbackSpeedPatch = bytecodePatch(
    name = "Enable speed control",
    description = "Enables experimental speed control to the video player.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsPlaybackSpeedFeatureEnabledFingerprint.method.returnEarly(true)

        IsPlaybackSettingsV2EnabledFingerprint.apply {
            val enableInstr = instructionMatches.last();
            val enableReg = enableInstr.getInstruction<OneRegisterInstruction>().registerA
            method.replaceInstruction(enableInstr.index, "const/4 v$enableReg, 0x1")
        }
    }
}