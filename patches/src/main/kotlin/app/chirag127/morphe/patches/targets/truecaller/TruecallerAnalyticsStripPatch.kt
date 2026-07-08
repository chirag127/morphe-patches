/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/misc/NeutralizeThirdPartySdksPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * Neutralizes 15 third-party analytics / ad SDK entry points. Each block is
 * wrapped in its own try/catch so partial failure (SDK removed from a future
 * Truecaller build) doesn't abort the whole patch — this is fail-soft by design.
 *
 * v1.3.0 note: Paresh's original used a `returnEarly` extension helper that
 * lives in his repo but not in ours. Inlined the equivalent `addInstructions`
 * calls here to stay dependency-free.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

// Inlined smali blocks (equivalents to Paresh's returnEarly helper)
private const val RETURN_VOID = "return-void"
private const val RETURN_FALSE = "const/4 v0, 0x0\nreturn v0"
private const val RETURN_TRUE = "const/4 v0, 0x1\nreturn v0"
private const val RETURN_NULL = "const/4 v0, 0x0\nreturn-object v0"

@Suppress("unused")
val truecallerAnalyticsStripPatch = bytecodePatch(
    name = "Truecaller — strip third-party analytics + ad SDKs",
    description = "No-ops third-party telemetry, ad-network, and tracking SDK init calls (Clarity, CleverTap, AppsFlyer, Moloco, HMS, Freshchat, InMobi, Appnext, Vungle, Fyber, Mintegral, IronSource, HyBid, MLKit, Presence stream).",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        // 1. Microsoft Clarity — return null
        try {
            mutableClassDefBy("Lcom/microsoft/clarity/Clarity;").methods
                .first { it.name == "initialize" && it.parameterTypes.size == 2 }
                .addInstructions(0, RETURN_NULL)
        } catch (_: Exception) { /* SDK absent */ }

        // 2. CleverTap — return null
        try {
            CleverTapFactoryFingerprint.method.addInstructions(0, RETURN_NULL)
        } catch (_: PatchException) { /* SDK absent */ }

        // 3. AppsFlyer — return void
        try {
            mutableClassDefBy("Lcom/appsflyer/internal/AFa1ySDK;").methods
                .first { it.name == "start" && it.parameterTypes.size == 1 && it.parameterTypes[0] == "Landroid/content/Context;" }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 4. Moloco — return void
        try {
            mutableClassDefBy("Lcom/moloco/sdk/publisher/Moloco;").methods
                .first { it.name == "initialize" && it.parameterTypes.size == 2 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 5. Huawei HMS InitProvider — return true
        try {
            mutableClassDefBy("Lcom/huawei/hms/aaid/InitProvider;").methods
                .first { it.name == "onCreate" && it.returnType == "Z" }
                .addInstructions(0, RETURN_TRUE)
        } catch (_: Exception) { /* SDK absent */ }

        // 6. Freshchat — return false
        try {
            mutableClassDefBy("Lcom/freshchat/consumer/sdk/Freshchat;").methods
                .first { it.name == "init" && it.returnType == "Z" }
                .addInstructions(0, RETURN_FALSE)
        } catch (_: Exception) { /* SDK absent */ }

        // 7. InMobi — return void
        try {
            mutableClassDefBy("Lcom/inmobi/sdk/InMobiSdk;").methods
                .first { it.name == "init" && it.parameterTypes.size == 4 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 8. Appnext — return void
        try {
            mutableClassDefBy("Lcom/appnext/nexdk/AppnextSDK;").methods
                .first { it.name == "initialize" && it.parameterTypes.size == 1 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 9. Vungle — return void
        try {
            VungleInitFingerprint.method.addInstructions(0, RETURN_VOID)
        } catch (_: PatchException) { /* SDK absent */ }

        // 10. Fyber — return void
        try {
            mutableClassDefBy("Lcom/fyber/inneractive/sdk/external/InneractiveAdManager;").methods
                .first { it.name == "initialize" && it.parameterTypes.size == 3 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 11. Mintegral — return void
        try {
            MintegralInitFingerprint.method.addInstructions(0, RETURN_VOID)
        } catch (_: PatchException) { /* SDK absent */ }

        // 12. IronSource — return void
        try {
            mutableClassDefBy("Lcom/ironsource/mediationsdk/IronSource;").methods
                .first { it.name == "init" && it.parameterTypes.size == 4 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 13. PubNative HyBid — return void
        try {
            mutableClassDefBy("Lnet/pubnative/lite/sdk/HyBid;").methods
                .first { it.name == "initialize" && it.parameterTypes.size == 3 }
                .addInstructions(0, RETURN_VOID)
        } catch (_: Exception) { /* SDK absent */ }

        // 14. Presence gRPC stream — return void
        try {
            PresenceStreamFingerprint.method.addInstructions(0, RETURN_VOID)
        } catch (_: PatchException) { /* SDK absent */ }

        // 15. Google ML Kit — return empty list
        try {
            mutableClassDefBy("Lcom/google/mlkit/common/internal/CommonComponentRegistrar;").methods
                .first { it.name == "getComponents" && it.returnType == "Ljava/util/List;" }
                .addInstructions(
                    0,
                    """
                        new-instance v0, Ljava/util/ArrayList;
                        invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V
                        return-object v0
                    """,
                )
        } catch (_: Exception) { /* SDK absent */ }
    }
}
