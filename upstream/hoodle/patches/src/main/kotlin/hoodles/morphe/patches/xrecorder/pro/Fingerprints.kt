package hoodles.morphe.patches.xrecorder.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

object GetProUsageFingerprint : Fingerprint(
    strings = listOf("XRecorder: ", "VIP;"),
    filters = listOf(
        methodCall(returnType = "Z"),
        opcode(Opcode.MOVE_RESULT),
        opcode(Opcode.IF_EQZ),
        string("VIP;")
    )
)

object SetDisableAdsUsageFingerprint : Fingerprint(
    strings = listOf("com.inshot.screenrecorder.week", "com.inshot.screenrecorder.month"),
    parameters = listOf("Z", "Ljava/util/List;"),
    filters = listOf(
        literal(0)
    )
)