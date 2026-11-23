package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.patches.filterStoriesListPatch
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val hideAllStoriesPatch = bytecodePatch(
    name = "Hide all stories",
    description = """
        Hides all stories from the tray in the feed page.
        You may need to clean app data after the initial use of this patch to see its effects.
    """,
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(filterStoriesListPatch)

    execute {
        filterAllStories()
    }
}
