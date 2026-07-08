/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Composition patch — original work by chirag127/morphe-patches. GPL-3.0-or-later.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

/**
 * Aggregate patch that enables every atomic Truecaller patch in this bundle.
 *
 * Individual patches all default = false, so users opting into `truecallerAllPatch`
 * get the full stack in one click; users who prefer a smaller surface can pick
 * atomic patches individually and leave this one off.
 *
 * Notes on scope:
 * - Fingerprints target Truecaller v26.10.6 (Paresh + Bufferk fanout).
 * - Every atomic patch is fail-soft (PatchException swallowed) so a version-drift
 *   fingerprint miss no-ops that one entry without aborting the build.
 * - Four STUB patches are included as dependencies to keep the surface reachable;
 *   they currently no-op but will start doing real work as fingerprints are authored.
 */
@Suppress("unused")
val truecallerAllPatch = bytecodePatch(
    name = "Truecaller — all patches",
    description = "Enables every atomic Truecaller patch above. Toggle individual patches to opt out.",
    default = true,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    dependsOn(
        // Paresh-derived
        truecallerHideAssistantTabPatch,
        truecallerHideFamilyProtectionButtonPatch,
        truecallerHidePremiumFromSettingsPatch,
        truecallerHidePremiumTabPatch,
        truecallerHideScamsTabPatch,
        truecallerTelemetryDisablePatch,
        truecallerUpdateNagDisablePatch,
        truecallerGmsSignInBypassPatch,
        truecallerAnalyticsStripPatch,
        truecallerPremiumUnlockPatch,
        // Bufferk-derived
        truecallerAdRemovalPatch,
        truecallerHidePremiumBannersPatch,
        truecallerHidePremiumUpgradeUiPatch,
        truecallerMockPremiumBadgePatch,
        truecallerRemovePremiumUiPatch,
        // Original stubs (no-op until fingerprints authored)
        truecallerContactUploadDisablePatch,
        truecallerGoogleDriveSyncPatch,
        truecallerWatermarkStripPatch,
        truecallerRegionUnlockPatch,
    )

    execute {
        // Composition patch. Every real work happens in the depended-on atomic patches.
    }
}
