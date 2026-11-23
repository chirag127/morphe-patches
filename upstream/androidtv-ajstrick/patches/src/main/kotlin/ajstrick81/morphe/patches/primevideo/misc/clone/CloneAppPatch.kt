package ajstrick81.morphe.patches.primevideo.misc.clone

import app.morphe.patcher.patch.resourcePatch
import ajstrick81.morphe.patches.primevideo.shared.Constants
import org.w3c.dom.Element

// Suffix appended to the original applicationId for the cloned package, e.g.
// com.amazon.amazonvideo.livingroom -> com.amazon.amazonvideo.livingroom.mod
// Matches the value confirmed working in the field report (manual apktool edit).
private const val CLONE_SUFFIX = "mod"

// ─────────────────────────────────────────────────────────────────────────────
// Clone Prime Video (side-by-side install)
//
// Many Android TVs / TV boxes ship Prime Video as a SYSTEM app that cannot be
// uninstalled without root. Installing a patched build over it is impossible,
// and installing the patched build UNDER a new applicationId fails with:
//
//   INSTALL_FAILED_CONFLICTING_PROVIDER: ... provider name
//   com.amazon.amazonvideo.livingroom.mobileadsinitprovider (in package
//   ...livingroom.mod) is already used by com.amazon.amazonvideo.livingroom
//
// Renaming the applicationId alone is NOT enough: a ContentProvider's
// android:authorities and any custom <permission> android:name must be UNIQUE
// device-wide, independent of applicationId. They stay pinned to the original
// package string, so PackageManager rejects the install as long as the stock
// app is present.
//
// This patch renames the package AND every package-scoped identifier that must
// be device-unique, so the clone installs cleanly alongside the stock app.
//
// Confirmed package-coupled identifiers in this build's manifest:
//   providers (android:authorities):
//     - com.amazon.amazonvideo.livingroom.provider
//     - com.amazon.amazonvideo.livingroom.mobileadsinitprovider  (GMS Ads)
//   permission (android:name):
//     - com.amazon.amazonvideo.livingroom.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION
//
// Intent actions (e.g. ...AMAZON_BUTTON) and <queries> package targets are left
// untouched: they are not device-unique and are matched by literal string on
// both ends, so rewriting them would break internal messaging without helping
// installation.
//
// Runs in finalize {} so the rename happens after all other patches, at manifest
// write time. Opt-in (default = false): only clone when explicitly selected.
// ─────────────────────────────────────────────────────────────────────────────
@Suppress("unused")
val cloneAppPatch = resourcePatch(
    name = "Clone Prime Video",
    description = "Renames the package to <original>.$CLONE_SUFFIX (and its provider " +
        "authorities / custom permissions) so the patched app installs side-by-side " +
        "with a non-removable system Prime Video. Opt-in.",
    default = false,
) {
    compatibleWith(Constants.COMPATIBILITY)

    finalize {
        val packageName = packageMetadata.packageName
        val newPackageName = "$packageName.$CLONE_SUFFIX"

        // Authorities may be an @string/... reference rather than a literal; any
        // such string resource names are collected here and rewritten below.
        val providerStringResources = mutableSetOf<String>()

        document("AndroidManifest.xml").use { document ->
            document.documentElement.setAttribute("package", newPackageName)

            // ── Custom permissions ──────────────────────────────────────────
            // A <permission> android:name must be device-unique. Rename each
            // declaration prefixed with the old package, and keep any matching
            // <uses-permission> in sync so the app can still request its own.
            val permissions = document.getElementsByTagName("permission")
            val usesPermissions = document.getElementsByTagName("uses-permission")

            for (i in 0 until permissions.length) {
                val permission = permissions.item(i) as? Element ?: continue
                val oldName = permission.getAttribute("android:name")
                val newName = when {
                    oldName.startsWith('.') -> continue
                    oldName.startsWith("$packageName.") ->
                        oldName.replaceFirst(packageName, newPackageName)
                    else -> "${newPackageName}_$oldName"
                }
                permission.setAttribute("android:name", newName)

                for (j in 0 until usesPermissions.length) {
                    val usePerm = usesPermissions.item(j) as? Element ?: continue
                    if (usePerm.getAttribute("android:name") == oldName) {
                        usePerm.setAttribute("android:name", newName)
                        break
                    }
                }
            }

            // ── Provider authorities ────────────────────────────────────────
            // android:authorities is a ';'-separated list; each entry must be
            // device-unique. Rewrite literals prefixed with the old package and
            // defer @string references to the strings.xml pass below.
            val providers = document.getElementsByTagName("provider")

            for (i in 0 until providers.length) {
                val provider = providers.item(i) as? Element ?: continue
                val authorities = provider.getAttribute("android:authorities").split(';')
                val newAuthorities = authorities.map {
                    when {
                        it.startsWith("$packageName.") ->
                            it.replaceFirst(packageName, newPackageName)
                        it.startsWith('@') -> {
                            providerStringResources.add(it.removePrefix("@string/"))
                            it
                        }
                        else -> "${newPackageName}_$it"
                    }
                }
                provider.setAttribute("android:authorities", newAuthorities.joinToString(";"))
            }
        }

        // ── @string-backed authorities ──────────────────────────────────────
        // No-op for the current build (both authorities are literals), but kept
        // so the patch survives a future build that moves them into strings.xml.
        if (providerStringResources.isNotEmpty()) {
            document("res/values/strings.xml").use { document ->
                val children = document.documentElement.childNodes
                for (i in 0 until children.length) {
                    val node = children.item(i) as? Element ?: continue

                    if (node.getAttribute("name") in providerStringResources) {
                        val authority = node.textContent
                        node.textContent = if (authority.startsWith("$packageName.")) {
                            authority.replaceFirst(packageName, newPackageName)
                        } else {
                            "${newPackageName}_$authority"
                        }
                    }
                }
            }
        }
    }
}
