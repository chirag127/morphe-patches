package hoodles.morphe.patches.camscanner.premium

import app.morphe.patcher.Fingerprint

object GetStatusCodeFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/comm/account_data/AccountPreference;",
    returnType = "J",
    strings = listOf("qp3sdjd79xhdas02sd")
)