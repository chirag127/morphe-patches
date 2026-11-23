package hoodles.morphe.patches.sleep.premium

import app.morphe.patcher.Fingerprint

object HasUnlockFingerprint : Fingerprint(
    definingClass = "Lcom/urbandroid/sleep/TrialFilter;",
    name = "hasUnlock"
)

object IsUnlockAckedFingerprint : Fingerprint(
    definingClass = "Lcom/urbandroid/common/util/PreferencesUtils;",
    name = "isUnlockAcked"
)

object IsTrialFingerprint : Fingerprint(
    definingClass = "Lcom/urbandroid/sleep/TrialFilter;",
    name = "isTrial"
)
