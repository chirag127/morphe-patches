package app.morphe.patches.instagram.patches.misc

import app.morphe.library.instagram.patches.removeBuildExpiredPopupPatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val removeBuildExpiredPopupPatch = removeBuildExpiredPopupPatch(
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}
