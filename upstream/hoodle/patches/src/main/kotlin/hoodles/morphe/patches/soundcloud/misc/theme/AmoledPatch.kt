package hoodles.morphe.patches.soundcloud.misc.theme

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.findElementByAttributeValue
import hoodles.morphe.patches.soundcloud.shared.Constants
import hoodles.morphe.util.addInstructionsToEnd
import org.w3c.dom.Element

private val bottomBarPatch = bytecodePatch {
    execute {
        BottomBarCtorFingerprint.method.addInstructionsToEnd("""
            sget p1, Landroid/graphics/Color;->BLACK:I
            invoke-virtual {p0, p1}, Landroid/view/View;->setBackgroundColor(I)V
        """.trimIndent())
    }
}

@Suppress("unused")
val amoledPatch = resourcePatch(
    name = "AMOLED dark theme",
    description = "Changes the default dark theme to use true blacks for AMOLED screens.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(bottomBarPatch)

    execute {
        val trueBlack = "@color/blackOT"

        // Background surface colors
        document("res/values/colors.xml").use { document ->
            val blackColors = listOf(
                "dark_mode_surface",
                "design_dark_default_color_background",
                "design_dark_default_color_surface"
            )

            val colors = document.getElementsByTagName("color")
            blackColors.forEach {
                colors.findElementByAttributeValue("name", it)?.textContent = trueBlack
            }
        }

        // Bottom mini player
        document("res/drawable/bth_footer_shape.xml").use { document ->
            val shapes = document.getElementsByTagName("shape")

            val solid = (shapes.item(0) as Element)
                .getElementsByTagName("solid")
                .item(0)
                .let {it as Element}
            solid.setAttribute("android:color", "@color/blackOT")

            val shape1 = shapes.item(1) as Element
            val gradient = shape1.getElementsByTagName("gradient").item(0)
            shape1.removeChild(gradient)
            shape1.appendChild(solid.cloneNode(false))
        }
    }
}