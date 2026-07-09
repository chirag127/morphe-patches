package hoodles.morphe.patches.goodreads.ads

import app.morphe.patcher.Fingerprint

object AdspBagsAdSectionFactoryFingerprint : Fingerprint(
    definingClass = "Lcom/goodreads/kindle/ui/sections/AdspBagsAdSection\$Companion;",
    name = "newInstance"
)

const val sectionListFragmentClass = "Lcom/goodreads/kindle/ui/fragments/sectionlist/SectionListFragment;"

object SectionListWithBannerAdFingerprint : Fingerprint(
    definingClass = "${sectionListFragmentClass.dropLast(1)}\$Builder;",
    name = "withBannerAd",
    parameters = listOf("L", "Z")
)

object SectionListMarkAdsHiddenFingerprint : Fingerprint(
    definingClass = sectionListFragmentClass,
    name = "markAdsHidden"
)

object SectionListMayShowAdFingerprint : Fingerprint(
    definingClass = sectionListFragmentClass,
    name = "mayShowAd"
)

object SectionListAdSectionFingerprint : Fingerprint(
    definingClass = sectionListFragmentClass,
    name = "addSection"
)

object ShouldInterceptRequestFingerprint : Fingerprint(
    definingClass = "Lcom/goodreads/kindle/ui/fragments/WebViewFragment\$createWebViewClient\$1;",
    name = "shouldInterceptRequest"
)
