/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches, bufferk/morphe-patches, and Binarymend/morphe-patches
 * Truecaller Fingerprints.kt files. Original licensed GPL-3.0-or-later. This file retains that license.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

// ============================================================================
// Layout / UI fingerprints (from Paresh v26.10.6)
// ============================================================================

// Targets hc2.c.a() — scam feed manager isEnabled check
object ScamFeedEnabledFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        methodCall(definingClass = "Lhc2/a;", name = "a"),
        methodCall(definingClass = "Lhc2/baz;", name = "a"),
    ),
)

// Targets gg1.c.w() — call assistant feature toggle
object AssistantFeatureFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("featureCallAssistant"),
    ),
)

// Targets qn2.a.a() — finds the method via the premium button field access
object PremiumPurchaseSupportedFingerprint : Fingerprint(
    returnType = "Ljava/util/List;",
    filters = listOf(
        fieldAccess(opcode = Opcode.IGET_OBJECT, definingClass = "Lqn2/a;", name = "d", type = "Lqn2/l;"),
    ),
)

// Targets CategoriesFragment$b.emit() — sets premium item visibility
object SettingsPremiumVisibilityFingerprint : Fingerprint(
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;", "Lj33/baz;"),
    filters = listOf(
        fieldAccess(opcode = Opcode.IGET_BOOLEAN, definingClass = "Lfh2/m0;", name = "a", type = "Z"),
    ),
)

// Targets gg1.c — family protect feature flag check
object FamilyProtectFeatureFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("featureFamilyProtect"),
    ),
)

// Targets zo2/baz.a() — Compose fn that renders "Premium member" block on UsersHome settings
object PremiumBlockComposeFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf(
        "Lcom/truecaller/usershome/presentaion/ui/components/featureditemsection/baz;",
        "Lkotlin/jvm/functions/Function0;",
        "Landroidx/compose/ui/b;",
        "Ll2/k;",
        "I",
    ),
    filters = listOf(
        string("premium_block"),
    ),
)

// ============================================================================
// Misc fingerprints (from Paresh v26.10.6)
// ============================================================================

// Finds ti1/g (InAppUpdateManagerImpl) via constructor parameter names
object InAppUpdateManagerClassFingerprint : Fingerprint(
    strings = listOf("playAppUpdateManager", "configsInventory"),
)

// Targets ti1/g.d(UpdateTrigger)Z — shouldTriggerUpdate check
object ShouldTriggerUpdateFingerprint : Fingerprint(
    classFingerprint = InAppUpdateManagerClassFingerprint,
    returnType = "Z",
    parameters = listOf("Lcom/truecaller/inappupdate/UpdateTrigger;"),
)

// Targets AppStartTracker.enableTracking() — prevents telemetry from activating
object AppStartTrackerFingerprint : Fingerprint(
    definingClass = "Lcom/truecaller/analytics/technical/AppStartTracker;",
    name = "enableTracking",
    returnType = "V",
)

// GMS sign-in bypass — forces SMS-based OTP verification
object OtpSmsApiSelectionFingerprint : Fingerprint(
    returnType = "Ljava/lang/Object;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "Lcom/truecaller/wizard/verification/otp/sms/OtpSmsApi;",
            name = "GOOGLE",
        ),
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "Lcom/truecaller/wizard/verification/otp/sms/OtpSmsApi;",
            name = "SMS",
        ),
    ),
)

// ============================================================================
// Third-party SDK fingerprints (from Paresh v26.10.6)
// ============================================================================

// CleverTap wrapper factory
object CleverTapFactoryFingerprint : Fingerprint(
    returnType = "L",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf(
        "Landroid/content/Context;",
        "Lcom/clevertap/android/sdk/CleverTapInstanceConfig;",
        "L",
    ),
)

// Vungle/LiftoffMonetize init
object VungleInitFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    parameters = listOf(
        "Landroid/content/Context;",
        "Ljava/lang/String;",
        "L",
    ),
    custom = { _, classDef ->
        classDef.type.startsWith("Lcom/vungle/ads/")
    },
)

// Mintegral init
object MintegralInitFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Ljava/util/Map;", "Landroid/content/Context;"),
    custom = { _, classDef ->
        classDef.interfaces.contains("Lcom/mbridge/msdk/MBridgeSDK;")
    },
)

// Presence stream start
object PresenceStreamFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    custom = { method, classDef ->
        classDef.interfaces.contains("Lcom/truecaller/presence/PresenceManager;") &&
            method.implementation != null &&
            method.implementation!!.registerCount >= 4
    },
)

// ============================================================================
// Premium fingerprints (from Paresh v26.10.6)
// ============================================================================

// Targets com.truecaller.premium.data.k.b() — the central isPremium check
object IsPremiumFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("isPremiumExpired"),
    ),
)

// Targets com.truecaller.premium.data.k.S1() — returns the premium tier type
object GetPremiumTierFingerprint : Fingerprint(
    returnType = "Lcom/truecaller/premium/data/tier/PremiumTierType;",
    parameters = listOf(),
    filters = listOf(
        string("premiumLevel"),
    ),
)

// Finds g02.k class via its constructor which has unique parameter names
object PremiumFeatureManagerClassFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("premiumFeaturesInventory", "qaPremiumFeatureHelper"),
)

// Targets g02.k.k(PremiumFeature, boolean) — master feature availability check
object IsFeatureAvailableFingerprint : Fingerprint(
    classFingerprint = PremiumFeatureManagerClassFingerprint,
    returnType = "Z",
    parameters = listOf("Lcom/truecaller/premium/data/feature/PremiumFeature;", "Z"),
)

// Targets com.truecaller.premium.data.g.h(z, continuation) — saves server state
object SavePremiumStateFingerprint : Fingerprint(
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Lzz1/z;", "Ll33/a;"),
    filters = listOf(
        methodCall(definingClass = "Lzz1/o1;", name = "X"),
        methodCall(definingClass = "Lzz1/o1;", name = "i"),
    ),
)

// Targets PremiumSubscriptionStatusNotificationWorker.doWork()
object SubscriptionNotificationWorkerFingerprint : Fingerprint(
    definingClass = "Lcom/truecaller/premium/PremiumSubscriptionStatusNotificationWorker;",
    name = "doWork",
    returnType = "Ljava/lang/Object;",
)

// Targets fh2.m0 toString — CategoriesSettingsState
object CategoriesSettingsStateFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    filters = listOf(
        string("CategoriesSettingsState(isPremiumItemVisible="),
    ),
)

// ============================================================================
// Bufferk fingerprints (v26.10.6) — kept where distinct from Paresh's set
// ============================================================================

// Targets Lcom/truecaller/acs/ui/baz;->Rh(Z)V — after-call ad update
object AfterCallMaybeUpdateAdFingerprint : Fingerprint(
    definingClass = "Lcom/truecaller/acs/ui/baz;",
    name = "Rh",
    returnType = "V",
    parameters = listOf("Z"),
)

// Targets Ltw1/f;->Th(Z)V — neo ACS ad update
object NeoAcsMaybeUpdateAdFingerprint : Fingerprint(
    definingClass = "Ltw1/f;",
    name = "Th",
    returnType = "V",
    parameters = listOf("Z"),
)

// CleverTap push event overloads (Bufferk analytics strip)
object CleverTapPushEventFingerprint : Fingerprint(
    definingClass = "Lkr0/k;",
    name = "push",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;"),
)

object CleverTapPushEventWithPropsFingerprint : Fingerprint(
    definingClass = "Lkr0/k;",
    name = "push",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Ljava/util/Map;"),
)

// Bufferk full-screen paywall onCreate
object FullScreenPaywallOnCreateFingerprint : Fingerprint(
    returnType = "V",
    name = "onCreate",
    parameters = listOf("Landroid/os/Bundle;"),
    custom = { _, classDef ->
        classDef.type.contains("FullScreenPaywall") ||
            classDef.type.contains("PaywallActivity")
    },
)

// Bufferk PremiumNavDrawerItemView.onAttachedToWindow
object PremiumNavDrawerItemViewFingerprint : Fingerprint(
    name = "onAttachedToWindow",
    returnType = "V",
    parameters = listOf(),
    custom = { _, classDef ->
        classDef.type.contains("PremiumNavDrawerItemView")
    },
)

// ============================================================================
// Binarymend fingerprints (v26.12.5) — kept as version-drift alt where distinct
// ============================================================================

// Loi1/baz; — ad feature toggle class (short obfuscated name; version-drift sensitive)
object AdsBazFingerprint : Fingerprint(
    definingClass = "Loi1/baz;",
)
