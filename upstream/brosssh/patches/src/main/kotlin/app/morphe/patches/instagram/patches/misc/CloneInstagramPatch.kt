package app.morphe.patches.instagram.patches.misc

import app.morphe.library.instagram.patches.clonePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val clonePatch = clonePatch(
    defaultPackageName = "com.instagram.android.brosssh",
    defaultAppName = "Instagram Distraction Free",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}
