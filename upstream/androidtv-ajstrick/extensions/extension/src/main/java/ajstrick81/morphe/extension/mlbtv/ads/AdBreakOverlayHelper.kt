package ajstrick81.morphe.extension.mlbtv.ads

import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Shows/hides a full-screen "Commercial Break in Progress" overlay over the
 * IMA SSAI ad-view-group, driven by the VideoStreamPlayer ad-break lifecycle
 * callbacks (Lb6/h$i;.onAdBreakStarted()/onAdBreakEnded() — see AtBatPatch.kt
 * Patch 5). This shields viewers from gambling ad content during live game
 * commercial breaks without touching any playback/DAI request plumbing,
 * so it carries none of the live-stream risk that blocking the SSAI session
 * outright (Patches 2/3/4) would.
 */
object AdBreakOverlayHelper {

    private const val TAG = "MORPHE-MLB-ADBREAK"

    private val mainHandler = Handler(Looper.getMainLooper())

    private var adViewGroup: ViewGroup? = null
    private var overlayView: FrameLayout? = null

    @JvmStatic
    fun registerAdViewGroup(viewGroup: ViewGroup) {
        adViewGroup = viewGroup
        Log.d(TAG, "registerAdViewGroup() — ad view group registered")
    }

    @JvmStatic
    fun showOverlay() {
        mainHandler.post {
            val container = adViewGroup ?: return@post
            val overlay = overlayView ?: buildOverlay(container).also { overlayView = it }
            if (overlay.parent == null) container.addView(overlay)
            overlay.visibility = View.VISIBLE
            overlay.bringToFront()
            Log.d(TAG, "showOverlay() — commercial break overlay shown")
        }
    }

    @JvmStatic
    fun hideOverlay() {
        mainHandler.post {
            overlayView?.visibility = View.GONE
            Log.d(TAG, "hideOverlay() — commercial break overlay hidden")
        }
    }

    private fun buildOverlay(container: ViewGroup): FrameLayout {
        val context = container.context
        val label = TextView(context).apply {
            text = "Commercial Break in Progress"
            setTextColor(Color.WHITE)
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }
        return FrameLayout(context).apply {
            setBackgroundColor(Color.BLACK)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            addView(
                label,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER,
                ),
            )
        }
    }
}
