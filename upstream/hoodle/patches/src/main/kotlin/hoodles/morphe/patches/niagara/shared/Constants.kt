package hoodles.morphe.patches.niagara.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "bitpit.launcher"

    val COMPATIBILITY = Compatibility(
        name = "Niagara Launcher",
        packageName = PACKAGE_NAME,
        appIconColor = 0x9fcdfb,
        targets = listOf(AppTarget("1.16.7"))
    )
}