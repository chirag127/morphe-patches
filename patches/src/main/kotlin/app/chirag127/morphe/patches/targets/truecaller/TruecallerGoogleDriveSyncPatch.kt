/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed via disassemble workflow — file placeholder only.
 * REQUIRES: MicroG-RE (Google-services re-implementation) or the SMS-only GmsSignInBypass patch.
 *
 * Intent: restore Truecaller's Google Drive backup / sync flow on re-signed APKs.
 * Truecaller uses Google Drive REST API via GMS Auth token; on a re-signed APK the
 * signature-tied client id no longer resolves, so backup silently fails.
 *
 * Two possible strategies:
 *   1. Redirect the GMS Auth call through MicroG-RE (userspace GMS shim)
 *   2. Rewrite the Drive REST client to use an app-embedded OAuth flow (invasive)
 *
 * TODO: disassemble v26.10.6, grep for:
 *   - "drive.googleapis.com", "https://www.googleapis.com/auth/drive.appdata"
 *   - GoogleSignInClient / GoogleAccountCredential usage
 *   - com.truecaller.backup.* worker classes
 * Then choose strategy based on where the auth token is fetched.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerGoogleDriveSyncPatch = bytecodePatch(
    name = "Truecaller — Google Drive sync fix (STUB)",
    description = "STUB (requires MicroG-RE): restore Google Drive backup / sync on re-signed APKs. Fingerprints not yet discovered.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        // TODO: implement once Drive-auth fingerprints are authored + MicroG-RE integration decided.
        // See file-level comment for grep starting points + strategy notes.
    }
}
