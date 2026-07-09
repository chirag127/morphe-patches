package hoodles.morphe.patches.windy.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

object IsPremiumForWidgetFingerprint : Fingerprint(
    filters = listOf(
        string("subscription"),
        string("", location = InstructionLocation.MatchAfterImmediately()),
        methodCall(
            parameters = listOf("Landroid/content/SharedPreferences;", "Ljava/lang/String;", "Ljava/lang/String;"),
            returnType = "Ljava/lang/String;",
            location = InstructionLocation.MatchAfterImmediately()
        )
    )
)

object ShouldInterceptRequestFingerprint : Fingerprint(
    definingClass = "/BridgeWebViewClient;",
    name = "shouldInterceptRequest"
)