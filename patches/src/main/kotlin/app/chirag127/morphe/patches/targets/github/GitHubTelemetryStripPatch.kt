/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed — file placeholder only.
 *
 * INTENT: no-op Firebase Analytics and the app-side telemetry wrapper in
 * `com.github.android`. Water-only scope: only the analytics init/log paths.
 * No network interception. No account-flow tampering. No push-notification
 * side-effects beyond stripping the analytics MODEL/MANUFACTURER attribution.
 *
 * Workflow disassembly (Action run 28978163106) confirms the analytics surface:
 *
 *   Firebase Analytics SDK present in smali_classes4/:
 *     - com/google/firebase/analytics/FirebaseAnalytics.smali          (public API)
 *     - com/google/firebase/analytics/connector/internal/AnalyticsConnectorRegistrar.smali
 *     - com/google/android/gms/measurement/AppMeasurement.smali        (impl)
 *     - com/google/android/gms/measurement/internal/e0.smali, t4.smali (reads Build.MODEL)
 *     - com/google/android/gms/internal/measurement/i3.smali, j3.smali (MODEL reads)
 *
 *   App-side telemetry wrapper:
 *     - smali_classes3/com/github/android/support/s.smali (Build.MANUFACTURER + MODEL at :112,:114)
 *     - smali_classes3/com/github/android/settings/r.smali (:228, :230)
 *     - smali/com/github/android/di/l.smali (:30)
 *     - smali/com/github/android/pushnotifications/f0.smali (:70, :72)
 *
 *   NO AdMob (google/android/gms/ads not present). NO Amplitude / Mixpanel /
 *   Segment / AppsFlyer / Braze. Firebase is the entire analytics footprint.
 *
 * PATCH STRATEGY (fail-soft, water-only):
 *   1. Stub `FirebaseAnalytics.logEvent(String, Bundle)` -> return-void
 *   2. Stub `FirebaseAnalytics.setUserProperty(String, String)` -> return-void
 *   3. Stub `FirebaseAnalytics.setUserId(String)` -> return-void
 *   4. Optional: no-op `AnalyticsConnectorRegistrar.getComponents()` return
 *      empty list — belt-and-braces prevents the SDK from registering.
 *   5. Optional: no-op the app-side wrapper in `com/github/android/support/s.smali`
 *      constructor / `attachBaseContext` path so MANUFACTURER/MODEL never sampled.
 *
 * DO NOT touch:
 *   - The `pushnotifications/f0.smali` MODEL reads unless they're purely
 *     analytics (verify: some may feed the FCM device-token registration which
 *     is not "telemetry" and stubbing breaks notifications).
 *   - Any Crashlytics init — different concern, not requested here.
 *
 * TODO:
 *   1. Author fingerprints for the three `FirebaseAnalytics` methods above
 *      (public API, stable names — should be straightforward).
 *   2. Fail-soft each fingerprint with its own try/catch so a mismatch on one
 *      doesn't skip the others.
 *   3. Add a soak test: after patch, confirm app boots and push notifications
 *      still fire (regression guard on the push-notifications-vs-telemetry
 *      distinction).
 */

package app.chirag127.morphe.patches.targets.github

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_GITHUB_ANDROID

@Suppress("unused")
val githubTelemetryStripPatch = bytecodePatch(
    name = "GitHub — telemetry strip (STUB)",
    description = "STUB: no-op Firebase Analytics logEvent/setUserProperty/setUserId. No network interception; water-only.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_GITHUB_ANDROID)

    execute {
        // TODO: implement once FirebaseAnalytics.{logEvent,setUserProperty,setUserId}
        // fingerprints are authored. Each in its own try/catch (PatchException) so
        // a version-drift miss on one method doesn't skip the others.
    }
}
