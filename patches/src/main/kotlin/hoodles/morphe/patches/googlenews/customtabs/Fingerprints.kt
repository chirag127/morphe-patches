package hoodles.morphe.patches.googlenews.customtabs

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.methodCall
import app.morphe.patcher.newInstance
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

object CustomTabsEnabledUsageFingerprint: Fingerprint(
    filters = listOf(
        string("https://www.youtube.com/watch?v=%s&t=%s"),
        opcode(Opcode.CHECK_CAST),
        opcode(Opcode.IGET_BOOLEAN, InstructionLocation.MatchAfterImmediately())
    )
)

object IsExperimentEnabledFingerprint: Fingerprint(
    strings = listOf("ArticleCustomTabs__use_supported_custom_tabs")
)

object IsSupportedBrowserFingerprint: Fingerprint(
    parameters = listOf(),
    returnType = "Ljava/lang/String;",
    filters = listOf(
        newInstance("Ljava/util/HashSet;"),
        methodCall(name = "contains"),
        opcode(Opcode.MOVE_RESULT, InstructionLocation.MatchAfterImmediately())
    )
)