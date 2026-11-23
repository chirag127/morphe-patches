package hoodles.morphe.patches.camscanner.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "com.intsig.camscanner"

    val COMPATIBILITY = Compatibility(
        name = "CamScanner",
        packageName = PACKAGE_NAME,
        appIconColor = 0X19BCAA,
        targets = listOf(AppTarget("7.20.0.2606230000"))
    )
}