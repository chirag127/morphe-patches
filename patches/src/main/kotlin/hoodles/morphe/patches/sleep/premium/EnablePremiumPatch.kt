package hoodles.morphe.patches.sleep.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Sleep as Android",
        packageName = "com.urbandroid.sleep",
        appIconColor = 0x80c583,
        targets = listOf(AppTarget("20260526"))
    ))

    execute {
        HasUnlockFingerprint.method.returnEarly(true)
        IsUnlockAckedFingerprint.method.returnEarly(true)
        IsTrialFingerprint.method.returnEarly(false)
    }
}