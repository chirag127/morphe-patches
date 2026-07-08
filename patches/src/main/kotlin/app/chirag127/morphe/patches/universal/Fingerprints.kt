package app.chirag127.morphe.patches.universal

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

/**
 * Matches any method that calls PackageManager.hasSystemFeature(String) —
 * how apps check for Pixel-only feature flags.
 *
 * We use a broad fingerprint because we don't know the exact caller class
 * in advance. Iterate the exception stack trace if this fails to match.
 */
object HasSystemFeatureFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        methodCall(
            definingClass = "Landroid/content/pm/PackageManager;",
            name = "hasSystemFeature",
        ),
    ),
)

/**
 * Matches Build.MANUFACTURER read. Used by apps that string-compare
 * against "Google" to gate Pixel-only paths.
 */
object BuildManufacturerFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    filters = listOf(
        string("MANUFACTURER"),
    ),
)

/**
 * Matches Build.MODEL read + Pixel string literal.
 */
object BuildModelPixelCheckFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("Pixel"),
    ),
)

/**
 * Matches any method containing common root-detection strings.
 * Broad on purpose — hits Magisk/su/xposed probes in most restricted apps.
 */
object RootDetectionFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        string("Magisk"),
    ),
)

/**
 * Matches Play Integrity API entry point.
 */
object IntegrityManagerFingerprint : Fingerprint(
    filters = listOf(
        methodCall(
            definingClass = "Lcom/google/android/play/core/integrity/IntegrityManager;",
        ),
    ),
)
