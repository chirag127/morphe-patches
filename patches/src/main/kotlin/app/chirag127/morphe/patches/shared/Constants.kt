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

    /** YouTube (Google). Piko/on-top variants layer on Morphe base. */
    val COMPATIBILITY_YOUTUBE: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.youtube" to null,
    )

    /** Twitter / X. */
    val COMPATIBILITY_TWITTER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.twitter.android" to null,
    )

    /** Instagram. */
    val COMPATIBILITY_INSTAGRAM: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.instagram.android" to null,
    )

    /** TikTok (musically package on Play Store). */
    val COMPATIBILITY_TIKTOK: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.zhiliaoapp.musically" to null,
    )

    /** Reddit official app. */
    val COMPATIBILITY_REDDIT: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.reddit.frontpage" to null,
    )

    /** Duolingo. */
    val COMPATIBILITY_DUOLINGO: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.duolingo" to null,
    )

    /** 1DM+ (paid downloader). */
    val COMPATIBILITY_1DMPLUS: Array<Pair<String, Set<String>?>> = arrayOf(
        "idm.internet.download.manager.plus" to null,
    )

    /** Moon+ Reader Pro. */
    val COMPATIBILITY_MOONREADER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.flyersoft.moonreaderp" to null,
    )

    /** Snapchat. */
    val COMPATIBILITY_SNAPCHAT: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.snapchat.android" to null,
    )

    /** WhatsApp. */
    val COMPATIBILITY_WHATSAPP: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.whatsapp" to null,
    )

    /** Google Maps. */
    val COMPATIBILITY_GOOGLEMAPS: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.google.android.apps.maps" to null,
    )

    /** Spotify. */
    val COMPATIBILITY_SPOTIFY: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.spotify.music" to null,
    )

    /** LinkedIn. */
    val COMPATIBILITY_LINKEDIN: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.linkedin.android" to null,
    )

    /** Amazon Shopping (India). */
    val COMPATIBILITY_AMAZONSHOPPING: Array<Pair<String, Set<String>?>> = arrayOf(
        "in.amazon.mShop.android.shopping" to null,
    )

    /** Amazon Prime Video. */
    val COMPATIBILITY_PRIMEVIDEO: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.amazon.avod.thirdpartyclient" to null,
    )

    /** Threads (Meta). */
    val COMPATIBILITY_THREADS: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.instagram.barcelona" to null,
    )

    /** Facebook. */
    val COMPATIBILITY_FACEBOOK: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.facebook.katana" to null,
    )

    /** Notion. */
    val COMPATIBILITY_NOTION: Array<Pair<String, Set<String>?>> = arrayOf(
        "notion.id" to null,
    )

    /** Todoist. */
    val COMPATIBILITY_TODOIST: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.todoist" to null,
    )

    /** Any.do. */
    val COMPATIBILITY_ANYDO: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.anydo" to null,
    )

    /** Xiaomi MIUI Music. */
    val COMPATIBILITY_MIUIMUSIC: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.miui.player" to null,
    )

    /** Xiaomi MIUI Weather. */
    val COMPATIBILITY_MIUIWEATHER: Array<Pair<String, Set<String>?>> = arrayOf(
        "com.miui.weather2" to null,
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
