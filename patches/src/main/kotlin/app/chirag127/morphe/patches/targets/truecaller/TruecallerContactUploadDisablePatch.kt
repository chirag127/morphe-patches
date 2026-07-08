/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed via disassemble workflow — file placeholder only.
 *
 * Intent: prevent Truecaller from uploading the local contact book to Truecaller
 * servers. Two probable attack surfaces:
 *   1. ContactsSync worker / periodic job — no-op the doWork() body
 *   2. Consent gate on first-run — force the "decline sharing" branch
 *
 * TODO: disassemble Truecaller v26.10.6 APK, grep for:
 *   - "contactsSync", "syncContacts", "uploadContacts", "phonebookSync"
 *   - "com.truecaller.contactsync.*" package
 *   - WorkManager subclasses under com.truecaller/[dir]/sync/
 * Then author fingerprints + return-early strategy per contact-upload path.
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerContactUploadDisablePatch = bytecodePatch(
    name = "Truecaller — disable contact upload (STUB)",
    description = "STUB: prevent local contact book from being uploaded to Truecaller servers. Fingerprints not yet discovered.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        // TODO: implement once ContactsSync fingerprints are authored.
        // See file-level comment for grep starting points.
    }
}
