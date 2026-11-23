package ajstrick81.morphe.patches.primevideo.misc.extension

import app.morphe.patcher.patch.bytecodePatch
import ajstrick81.morphe.patches.primevideo.shared.Constants

// Merges extensions/extension's compiled dex (packaged as
// extensions/extension.mpe by the :extensions:extension module) into the
// patched APK before SkipAdsPatch's invoke-static calls run. extendWith is
// the only thing that populates Patch.extensionInputStream — there is no
// automatic merge based on the settings.gradle.kts extensions block.
val primeVideoExtensionPatch = bytecodePatch(
    name = "Prime Video extension",
    description = "Integrates the Prime Video ATV extension for ad group skipping.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    extendWith("extensions/extension.mpe")

    execute {}
}
