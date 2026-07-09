package hoodles.morphe.patches.xrecorder.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "videoeditor.videorecorder.screenrecorder"

    val COMPATIBILITY = Compatibility(
        name = "XRecorder",
        packageName = PACKAGE_NAME,
        appIconColor = 0xf76219,
        targets = listOf(AppTarget("2.5.1.1"))
    )
}