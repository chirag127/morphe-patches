package ajstrick81.morphe.patches.peacock.misc.security

import app.morphe.patcher.patch.resourcePatch
import ajstrick81.morphe.patches.peacock.shared.Constants

// ─────────────────────────────────────────────────────────────────────────────
// Disable Auto Updates
//
// Prevents Google Play Store from automatically updating and replacing the
// patched APK with the official unpatched version.
//
// Without this patch, if the user has Peacock in their Play Store library, an
// automatic update would silently reinstall the official APK and remove the
// patch entirely — restoring the ad stack — with no warning to the user.
//
// Implementation:
//   There is no manifest attribute that turns off Play Store auto-updates —
//   update eligibility is decided entirely by Play Store comparing versionCode
//   against what's installed, not by anything the app declares about itself.
//   So instead, this patch bumps android:versionCode on the <manifest> root
//   well past the real app's current value. Play Store only offers/pushes an
//   update when its listed versionCode is higher than what's installed, so
//   keeping the patched build's versionCode artificially ahead makes Play
//   Store treat it as already up to date.
//
// Note: This does not block manually installing a different APK. To update the
// patched APK, users need to download a new version and re-patch it via Morphe.
// Also note (per the upstream reference): this technique does not help when the
// app is installed by mounting.
// ─────────────────────────────────────────────────────────────────────────────

@Suppress("unused")
val disableAutoUpdatesPatch = resourcePatch(
    name = "Disable auto-updates",
    description = "Stops the Google Play Store from silently updating Peacock back to the " +
        "official version and wiping out the patch (which would bring the ads back). Works by " +
        "setting the patched build's version number far ahead of anything on the Store, so it's " +
        "treated as already up to date. You can still update deliberately by re-patching a newer " +
        "APK in Morphe. Recommended to leave ON. Does not apply to mount-installed apps.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {

        // ─────────────────────────────────────────────────────────────────────
        // Bump android:versionCode on <manifest> past Play Store's real value
        //
        // Play Store only offers an update when its versionCode is higher than
        // what's installed, so pushing this far ahead keeps the patched build
        // looking newer than anything Play Store could offer for a long time.
        // Clamped to Int.MAX_VALUE since versionCode is a 32-bit signed field.
        // ─────────────────────────────────────────────────────────────────────
        document("AndroidManifest.xml").use { document ->
            val manifestNode = document
                .getElementsByTagName("manifest")
                .item(0)

            val versionCodeAttr = manifestNode
                .attributes
                .getNamedItem("android:versionCode")

            val bumpedVersionCode = (versionCodeAttr.nodeValue.toLong() + 10_000_000L)
                .coerceAtMost(Int.MAX_VALUE.toLong())

            versionCodeAttr.nodeValue = bumpedVersionCode.toString()
        }
    }
}
