package ajstrick81.morphe.patches.foxone.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Fox One Android TV",
        packageName = "com.fox.foxone",
        appIconColor = 0x030B1A,
        targets = listOf(AppTarget("1.9.2"))
    )
}
