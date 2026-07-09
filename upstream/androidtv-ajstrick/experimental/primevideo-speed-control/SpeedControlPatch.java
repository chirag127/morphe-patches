package ajstrick81.morphe.extension.primevideo.speed;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

import androidx.media3.common.Player;

/**
 * Prime Video ATV — experimental playback-speed control.
 *
 * Unlike the phone app (com.amazon.avod.thirdpartyclient), the ATV build
 * (com.amazon.amazonvideo.livingroom) ships NO com.amazon.video.sdk settings
 * overlay, so hoodles' "flip the PlaybackSpeedFeatureConfig flag" technique
 * has nothing to unlock here — that whole feature is simply absent from the
 * ATV dex set. This extension instead drives ExoPlayer directly.
 *
 * The ATV app renders its player UI through the native/WASM Ignite engine and
 * owns its media3 ExoPlayer instance inside
 * com.amazon.livingroom.mediapipelinebackend.MediaPipelineBackendEngine.
 * Playback commands (play/pause/seek) are invoked from native via JNI, so
 * there is no Java-side transport control to reuse — but the ExoPlayer object
 * itself is a stock androidx.media3 Player.
 *
 * Wiring (see EnableSpeedPatch.kt):
 *
 *   setCurrentPlayer  — captured right after the ExoPlayer is built in
 *                       MediaPipelineBackendEngine.lambda$init$5.
 *   clearCurrentPlayer— cleared in releasePlayerInternal when the player is
 *                       released, so we never touch a dead instance.
 *   onKeyEvent        — invoked at the top of
 *                       IgniteRenderer$EventHandler.onKey(View,int,KeyEvent),
 *                       the single Java chokepoint where remote key events are
 *                       forwarded into WASM. Returning true consumes the key so
 *                       the WASM layer never sees it.
 *
 * Why speed control matters for ads: media3's SSAI ad enforcement only ever
 * gated seekTo() while isPlayingAd() is true — it was never wired to
 * setPlaybackSpeed(). So cranking speed during an ad segment that survives the
 * SkipAds/DNS layers (e.g. the WASM-rendered ones) collapses it to a few
 * seconds of wall-clock time. This is a viewing-experience workaround, not an
 * additional impression-suppression layer.
 */
@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public final class SpeedControlPatch {

    private static final String TAG = "PVSpeedControl";

    // Speed ladder cycled through by the fast-forward / rewind remote keys.
    // Index 0 (1.0x) is normal speed.
    private static final float[] SPEEDS = {1.0f, 1.5f, 2.0f, 4.0f, 8.0f, 16.0f};

    private static volatile Player currentPlayer;
    private static volatile int speedIndex = 0;

    private SpeedControlPatch() {}

    /**
     * Captures the live ExoPlayer instance immediately after it is built.
     * Called from MediaPipelineBackendEngine.lambda$init$5.
     */
    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
        speedIndex = 0;
        Log.i(TAG, "captured player=" + player);
    }

    /**
     * Drops the reference when the player is released, so a stale key press
     * can never reach a dead player. Called from
     * MediaPipelineBackendEngine.releasePlayerInternal.
     */
    public static void clearCurrentPlayer() {
        currentPlayer = null;
        speedIndex = 0;
        Log.i(TAG, "cleared player");
    }

    /**
     * Intercepts remote key events before they reach the WASM Ignite layer.
     *
     * @return true if we handled the key (caller must return true / consume it),
     *         false to let the app process it normally.
     */
    public static boolean onKeyEvent(int keyCode, KeyEvent event) {
        try {
            if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            }

            final Player player = currentPlayer;
            if (player == null) {
                return false;
            }

            final int delta;
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    delta = 1;
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    delta = -1;
                    break;
                default:
                    return false;
            }

            int next = speedIndex + delta;
            if (next < 0) next = 0;
            if (next > SPEEDS.length - 1) next = SPEEDS.length - 1;
            speedIndex = next;
            final float speed = SPEEDS[speedIndex];

            // ExoPlayer enforces thread affinity — mutations must run on the
            // looper the player was created with, not the (main) thread this
            // key event arrives on.
            final Looper looper = player.getApplicationLooper();
            new Handler(looper).post(new SetSpeedTask(player, speed));

            Log.i(TAG, "key=" + keyCode + " -> requested speed=" + speed);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "onKeyEvent failed", e);
            return false;
        }
    }

    /** Applies the speed change on the player's own looper thread. */
    private static final class SetSpeedTask implements Runnable {
        private final Player player;
        private final float speed;

        SetSpeedTask(Player player, float speed) {
            this.player = player;
            this.speed = speed;
        }

        @Override
        public void run() {
            try {
                player.setPlaybackSpeed(speed);
                Log.i(TAG, "applied playback speed=" + speed);
            } catch (Exception e) {
                Log.e(TAG, "setPlaybackSpeed failed", e);
            }
        }
    }
}
