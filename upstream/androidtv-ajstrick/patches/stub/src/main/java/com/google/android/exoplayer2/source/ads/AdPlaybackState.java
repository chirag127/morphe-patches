package com.google.android.exoplayer2.source.ads;

/**
 * Compile-time stub for com.google.android.exoplayer2.source.ads.AdPlaybackState.
 *
 * This class is present in the patched app's DEX (classes3.dex, inside the
 * bundled GMS Ads SDK) and is referenced by SkipAdsPatch.java. The stub
 * provides just enough surface for javac to resolve the types and method
 * signatures used in the extension.
 *
 * The ExoPlayer2 AdPlaybackState API is structurally identical to the media3
 * version — same public fields and withRemovedAdGroupCount contract — but lives
 * in a separate package and separate DEX file.
 *
 * DO NOT add implementation logic — this class is never shipped to the device.
 * The real class in the patched APK's DEX is used at runtime.
 */
@SuppressWarnings("unused")
public final class AdPlaybackState {

    /** Total number of ad groups, including those already removed (live streams). */
    public final int adGroupCount;

    /**
     * Number of ad groups that have been removed from the start of the timeline.
     * Used in live streams where earlier groups are pruned as playback advances.
     * Active group indices start at this value.
     */
    public final int removedAdGroupCount;

    /**
     * Returns a copy of this state with the number of removed ad groups set to
     * {@code removedAdGroupCount}. Ad groups with indices between 0 (inclusive)
     * and {@code removedAdGroupCount} (exclusive) are dropped from the timeline.
     *
     * SkipAdsPatch calls this with {@code adGroupCount} to drop every ad group
     * from the SSAI schedule before ExoPlayer sees the map.
     *
     * @param removedAdGroupCount New count of removed ad groups (must be >= the
     *                            current value).
     * @return A new AdPlaybackState with the leading ad groups removed.
     */
    public AdPlaybackState withRemovedAdGroupCount(int removedAdGroupCount) {
        throw new UnsupportedOperationException("stub");
    }
}
