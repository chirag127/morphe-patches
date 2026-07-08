/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Adapted from Paresh-Maheshwari/paresh-patches/patches/src/main/kotlin/app/paresh/patches/truecaller/misc/GmsSigninBypassPatch.kt.
 * Original licensed GPL-3.0-or-later. This file retains that license.
 *
 * Key patch for the re-signed APK use case — forces SMS-based OTP verification
 * instead of GMS SmsRetriever, which breaks when the APK signature no longer
 * matches the one Google Play Services expects.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerGmsSignInBypassPatch = bytecodePatch(
    name = "Truecaller — GMS sign-in bypass",
    description = "Forces SMS-based OTP verification instead of GMS SmsRetriever, fixing sign-in on re-signed APKs.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        try {
            val googleFieldIndex = OtpSmsApiSelectionFingerprint.instructionMatches[0].index
            OtpSmsApiSelectionFingerprint.method.addInstructions(
                googleFieldIndex,
                """
                    sget-object v0, Lcom/truecaller/wizard/verification/otp/sms/OtpSmsApi;->SMS:Lcom/truecaller/wizard/verification/otp/sms/OtpSmsApi;
                    return-object v0
                """,
            )
        } catch (_: PatchException) {
            // Fingerprint mismatch. No-op.
        }
    }
}
