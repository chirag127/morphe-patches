package ajstrick81.morphe.patches.pluto.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Pluto TV Android TV",
        packageName = "tv.pluto.android",
        // accent_color from APKMirror bundle metadata (info.json)
        appIconColor = 0xB8B004,
        // Tested against 5.66.0-leanback (versionCode 510400248)
        targets = listOf(AppTarget("5.66.0-leanback"))
    )
}
