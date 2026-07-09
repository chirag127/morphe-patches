package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.literal
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

// The hash code of the field of interest. It is used as the key of a hashmap
private val hashedFieldInteger = "enable_media_notes_production".hashCode()

private object FeedResponseMediaParserFingerprint : Fingerprint(
    filters = listOf(
        literal(hashedFieldInteger),
        string("enable_media_notes_production"),
    ),
    returnType = "Ljava/lang/Boolean;"
)

private object LiveTreeGetOptionalBooleanFingerprint : Fingerprint (
    name = "getOptionalBooleanValueByHashCode",
    definingClass = "Lcom/instagram/pando/livetree/LiveTreeJNI;"
)

@Suppress("unused")
val hideReshareButtonPatch = bytecodePatch(
    name = "Hide reshare button",
    description = "Hides the reshare button from both posts and reels.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {

        FeedResponseMediaParserFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object v0
            """
        )

        // If it's trying to get the value for our field of interest via the Pando native library,
        // force the value to false instead
        LiveTreeGetOptionalBooleanFingerprint.method.addInstructions(
            0,
            """
                const v0, $hashedFieldInteger
                if-ne p1, v0, :nopatch
                sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object v0
                :nopatch
                nop
        """
        )
    }
}
