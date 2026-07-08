package app.chirag127.morphe.patches.shared

/**
 * Package specifiers for compatibleWith() on patches.
 * Each entry is Pair(package-name, versions).
 * Versions = emptyArray() means "any version".
 */
object Constants {
    /** Google Pixel Studio (Pixel-9-exclusive image gen). */
    val COMPATIBILITY_PIXEL_STUDIO = arrayOf(
        "com.google.android.apps.pixel.creativeassistant" to emptyArray<String>(),
    )

    /** Universal — applies to any APK. Used by device-detection remover. */
    val COMPATIBILITY_UNIVERSAL = arrayOf(
        "*" to emptyArray<String>(),
    )

    /** Common Pixel-experience feature strings the universal patches check for. */
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
