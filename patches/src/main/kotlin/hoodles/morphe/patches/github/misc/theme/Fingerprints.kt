package hoodles.morphe.patches.github.misc.theme

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

object FunctionalColorsCtorFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("GitHubFunctionalColors(backgroundPrimary=")
    ),
    name = "<init>"
)

object SetNavigationBarContrastFingerprint : Fingerprint(
    strings = listOf("statusBarStyle", "navigationBarStyle", "window", "view"),
    filters = listOf(
        methodCall(name = "setNavigationBarContrastEnforced")
    )
)