package app.chirag127.morphe.patches.shared

/**
 * Package specifiers for compatibleWith() on patches.
 * Overload signature: vararg packages: Pair<String, Set<String>?>
 * Pass `null` (or an empty set) to mean "any version".
 */
object Constants {
    /** Google Pixel Studio (Pixel-9-exclusive image gen). */
    val COMPATIBILITY_PIXEL_STUDIO: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.pixel.creativeassistant" to null,
    )

    /** Google Pixel Recorder (Pixel-only voice recorder with AI transcription). */
    val COMPATIBILITY_PIXEL_RECORDER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.recorder" to null,
    )

    /** Google Pixel Weather (Pixel-9-exclusive weather app). */
    val COMPATIBILITY_PIXEL_WEATHER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.weather" to null,
    )

    /**
     * Google Pixel Screenshots (Pixel-8+-exclusive on-device AI screenshot library).
     * Package is `com.google.android.apps.pixel.agent` per prior research.
     */
    val COMPATIBILITY_PIXEL_SCREENSHOTS: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.pixel.agent" to null,
    )

    /** Truecaller. Version pinning left null (any); atomic patches target v26.10.6 fingerprints. */
    val COMPATIBILITY_TRUECALLER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.truecaller" to null,
    )

    /**
     * Google News (com.google.android.apps.magazines). No traditional AdMob
     * entrypoints in app code — ad surfaces are native feed cards keyed by
     * SPONSORED_ARTICLE_AD enum in obfuscated class `akxk`, plus IMA SDK for
     * video/pause ads and `com.google.android.gms.ads.adshield` for integrity.
     * Version pin left null; atomic patches are STUBs pending fingerprint work.
     */
    val COMPATIBILITY_GOOGLE_NEWS: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.magazines" to null,
    )

    /**
     * GitHub Android app (closed source; Play Store only). Version pin left null; atomic
     * patches target color palette entries in `res/values-night/colors.xml` and view-model
     * anchors observed in the workflow-disassembled v1.255.x smali (see
     * `disassembled-com.github.android` artifact: 108 files under `smali/com/github/android/comment/`
     * plus preserved-name `ComposeCommentBottomSheetDialog.smali`; Firebase Analytics present
     * in `smali_classes4/com/google/firebase/analytics/`).
     */
    val COMPATIBILITY_GITHUB_ANDROID: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.github.android" to null,
    )

    /** Common Pixel-experience feature strings that in-APK checks target. */
    val PIXEL_FEATURE_STRINGS = listOf(
        "com.google.android.feature.PIXEL_EXPERIENCE",
        "com.google.android.feature.PIXEL_2017_EXPERIENCE",
        "com.google.android.feature.PIXEL_2018_EXPERIENCE",
        "com.google.android.feature.PIXEL_2019_EXPERIENCE",
        "com.google.android.feature.PIXEL_2020_EXPERIENCE",
        "com.google.android.feature.PIXEL_2021_EXPERIENCE",
        "com.google.android.feature.PIXEL_2022_EXPERIENCE",
        "com.google.android.feature.PIXEL_2023_EXPERIENCE",
        "com.google.android.feature.PIXEL_2024_EXPERIENCE",
        "com.google.android.feature.PIXEL_2025_EXPERIENCE",
        "com.google.android.feature.PIXEL_2026_EXPERIENCE",
        "com.google.android.feature.PIXEL_2027_EXPERIENCE",
        "com.google.android.feature.GOOGLE_BUILD",
        "com.google.android.feature.GOOGLE_EXPERIENCE",
        "com.google.android.feature.TURBO_PREMIUM",
    )
}
