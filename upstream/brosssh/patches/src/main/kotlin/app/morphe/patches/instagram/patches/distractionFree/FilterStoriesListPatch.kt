package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.patches.filterStories
import app.morphe.patcher.patch.BytecodePatchContext

context(_: BytecodePatchContext)
internal fun filterSuggestedStories() = filterStories(
    "suggested_user_reel",
    "suggested_user",
    "suggested_creator_reel"
)

context(_: BytecodePatchContext)
internal fun filterHighlightedStories() = filterStories(
    "highlight_rewind_reel"
)


context(_: BytecodePatchContext)
internal fun filterAllStories() {
    filterSuggestedStories()
    filterHighlightedStories()
    filterStories("user_reel")
}
