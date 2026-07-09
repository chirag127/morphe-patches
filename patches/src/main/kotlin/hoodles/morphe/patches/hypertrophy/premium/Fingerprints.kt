package hoodles.morphe.patches.hypertrophy.premium

import app.morphe.patcher.Fingerprint

object WebViewClientClassFingerprint : Fingerprint(
    name = "onPageFinished",
    strings = listOf("webView")
)