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

    /** Truecaller. Version pinning left null (any); atomic patches target v26.10.6 fingerprints. */
    val COMPATIBILITY_TRUECALLER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.truecaller" to null,
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
