package hoodles.morphe.patches.duolingo.misc.icon

import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.asSequence
import app.morphe.util.removeFromParent
import hoodles.morphe.patches.duolingo.shared.Constants
import hoodles.morphe.patches.duolingo.shared.integrity.disableLoginIntegrityPatch
import org.w3c.dom.Element

@Suppress("unused")
val disableDynamicIconPatch = resourcePatch(
    name = "Disable dynamic app icon",
    description = "Prevents Duolingo from changing the app icon. Only the default icon will be available.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableLoginIntegrityPatch)

    execute {
        document("AndroidManifest.xml").use { document ->
            val toRemove = document.getElementsByTagName("activity-alias")
                .asSequence()
                .filterIsInstance<Element>()
                .filter {
                    it.getAttribute("android:enabled") == "false" &&
                            it.getAttribute("android:targetActivity") == "com.duolingo.splash.LaunchActivity"
                }
                .toList()

            toRemove.forEach {
                println(it)
                it.removeFromParent()
            }
        }
    }
}