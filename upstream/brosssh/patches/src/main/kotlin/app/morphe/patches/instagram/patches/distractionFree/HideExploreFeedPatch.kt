package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.utility.JsonParserFingerprint
import app.morphe.library.instagram.utility.replaceJsonFieldWithBogus
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

private object ExploreResponseJsonParserFingerprint : JsonParserFingerprint(
    "sectional_items",
    "clusters"
)


@Suppress("unused")
val hideExploreFeedPatch = bytecodePatch(
    name = "Hide explore feed",
    description = "Hides posts and reels from the explore/search page.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        ExploreResponseJsonParserFingerprint.match().replaceJsonFieldWithBogus()
    }
}
