package hoodles.morphe.patches.googlenews.misc.gms

import hoodles.morphe.patches.googlenews.shared.Constants
import hoodles.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val GmsCoreSupportPatch = gmsCoreSupportPatch(
    mainOnCreateFingerprint = MainActivityOnCreateFingerprint,
    spoofedPackageSignature = "38918a453d07199354f8b19af05ec6562ced5788",
) {
    compatibleWith(Constants.COMPATIBILITY)
}