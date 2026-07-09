package hoodles.morphe.patches.primevideo.speed

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

object IsPlaybackSettingsV2EnabledFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/video/sdk/stores/overlays/settings/PlaybackSettingsV2Config;",
    filters = listOf(
        string("playbackSettingsV2_isEnabled"),
        opcode(Opcode.CONST_4, InstructionLocation.MatchAfterImmediately())
    )
)

object IsPlaybackSpeedFeatureEnabledFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/video/sdk/stores/overlays/settings/features/playbackspeed/store/PlaybackSpeedFeatureConfig;",
    name = "isPlaybackSpeedFeatureEnabled"
)