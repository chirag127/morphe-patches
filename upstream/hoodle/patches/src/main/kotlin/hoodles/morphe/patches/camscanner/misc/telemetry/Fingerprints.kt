package hoodles.morphe.patches.camscanner.misc.telemetry

import app.morphe.patcher.Fingerprint

object IsSkipLoggingFingerprint : Fingerprint (
    definingClass = "Lcom/intsig/log/LogAgentHelper;",
    parameters = listOf(),
    returnType = "Z"
)

object LogAgentRecordFingerprint : Fingerprint (
    definingClass = "Lcom/intsig/logagent/LogAgent;",
    name = "record"
)