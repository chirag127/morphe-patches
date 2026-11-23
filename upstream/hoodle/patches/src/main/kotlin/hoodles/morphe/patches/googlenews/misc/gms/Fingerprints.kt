package hoodles.morphe.patches.googlenews.misc.gms

import app.morphe.patcher.Fingerprint

object MainActivityOnCreateFingerprint : Fingerprint(
    strings = listOf("StartActivity onCreate")
)