package hoodles.morphe.patches.googlenews.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Google News",
        packageName = "com.google.android.apps.magazines",
        appIconColor = 0x4285F4,
        targets = listOf(
            AppTarget(version = "5.161.0.931240252"),
        ),
    )
}