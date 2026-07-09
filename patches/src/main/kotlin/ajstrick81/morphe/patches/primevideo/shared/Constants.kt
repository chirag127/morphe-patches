package ajstrick81.morphe.patches.primevideo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Prime Video Android TV",
        packageName = "com.amazon.amazonvideo.livingroom",
        appIconColor = 0x177BCE,
        targets = listOf(AppTarget("6.23.23+v15.5.0.70-armv7a"))
    )
}
