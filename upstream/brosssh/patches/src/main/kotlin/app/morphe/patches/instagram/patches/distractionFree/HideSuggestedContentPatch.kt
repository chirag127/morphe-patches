package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.patches.blockUrl
import app.morphe.library.instagram.patches.blockUrlBasePatch
import app.morphe.library.instagram.patches.filterStoriesListPatch
import app.morphe.library.instagram.patches.overrideMobileConfigBooleanFlag
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "Choose the suggested content to hide using the patch options.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    val hideSuggestedReels by booleanOption(
        key = "hideSuggestedReels",
        default = true,
        title = "Hide suggested reels",
        description = "Hides suggested reels from feed and reels tab."
    )

    val hideSuggestedStories by booleanOption(
        key = "hideSuggestedStories",
        default = true,
        title = "Hide suggested stories",
        description = "Hides suggested stories/users from story tray."
    )

    val hideHighlightStories by booleanOption(
        key = "hideHighlightStories",
        default = true,
        title = "Hide highlights in stories",
        description = "Hides highlighted stories from story tray, sponsored stories which have no set expiration."
    )

    val hideSuggestedAccount by booleanOption(
        key = "hideSuggestedAccount",
        default = true,
        title = "Hide suggested accounts",
        description = "Hides suggested accounts from the list of followers and from other profiles."
    )

    dependsOn(
        overrideMobileConfigBooleanFlag(
            // Hides suggestions in search box
            override = "111509::3" to false // ig_search_ta_nullstate_suggestions::is_android_enabled
        ),
        filterStoriesListPatch,
        blockUrlBasePatch
    )

    execute {
        if (hideSuggestedReels == true) hideSuggestedReelsPatch()

        if (hideSuggestedStories == true) filterSuggestedStories()

        if (hideHighlightStories == true) filterHighlightedStories()

        if (hideSuggestedAccount == true) blockUrl(
            "/discover/ayml",
            "/discover/chaining"
        )
    }
}
