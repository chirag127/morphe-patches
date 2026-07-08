package app.chirag127.morphe.patches.universal

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * NARROW fingerprints designed to avoid matching Activity/Application
 * lifecycle methods (which cause SuperNotCalledException when patched).
 *
 * Strategy: match on characteristic method SHAPE (return type, params,
 * access flags, instruction filters), not just string content.
 */

/**
 * Matches a method whose sole purpose appears to be "does this system
 * have a Pixel feature?" — i.e., returns boolean, single String param,
 * public/private (not Application.onCreate which is protected void),
 * body calls PackageManager.hasSystemFeature.
 *
 * Excludes lifecycle callbacks by requiring:
 * - returnType = Z (not V)  — lifecycle callbacks return void
 * - parameters = [String]   — lifecycle callbacks take Bundle
 */
object HasSystemFeatureCheckerFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
    accessFlags = listOf(AccessFlags.PUBLIC),
    filters = listOf(
        methodCall(
            definingClass = "Landroid/content/pm/PackageManager;",
            name = "hasSystemFeature",
        ),
    ),
)

/**
 * Matches a boolean method whose bytecode references the "Pixel"
 * string literal — the specific "is this a Pixel?" branch.
 *
 * Narrowed by:
 * - returnType = Z             — not void, not Object
 * - parameters = []            — no-arg check, excludes onCreate(Bundle)
 * - accessFlags PUBLIC STATIC  — utility method shape
 */
object PixelModelCheckerFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    filters = listOf(
        string("Pixel"),
    ),
)

/**
 * Matches a method that reads Build.MANUFACTURER — canonical
 * device-manufacturer check. Narrow to boolean-returning utility
 * methods to avoid matching side-effecting lifecycle code.
 */
object BuildManufacturerCheckerFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    filters = listOf(
        string("MANUFACTURER"),
    ),
)

/**
 * Root detection: methods returning boolean that reference "Magisk"
 * or "/system/xbin/su" string literals. Narrow with STATIC to
 * exclude any instance-method that could be a lifecycle callback.
 */
object RootDetectionCheckerFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    filters = listOf(
        string("Magisk"),
    ),
)
