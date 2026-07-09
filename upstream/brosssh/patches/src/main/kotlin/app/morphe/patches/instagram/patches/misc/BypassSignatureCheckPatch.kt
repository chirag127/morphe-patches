package app.morphe.patches.instagram.patches.misc

import app.morphe.library.instagram.patches.bypassSignatureCheckPatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val bypassSignatureCheckPatch = bypassSignatureCheckPatch(
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}

