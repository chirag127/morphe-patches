/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/misc/login/disablerequirement/Fingerprints.kt
 */
package app.morphe.patches.tiktok.misc.login.disablerequirement

import app.morphe.patcher.Fingerprint

internal object MandatoryLoginServiceFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.endsWith("/MandatoryLoginService;") && method.name == "enableForcedLogin"
    },
)

internal object MandatoryLoginService2Fingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.endsWith("/MandatoryLoginService;") && method.name == "shouldShowForcedLogin"
    },
)

