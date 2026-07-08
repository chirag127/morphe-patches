/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Original work: chirag127/morphe-patches. GPL-3.0-or-later.
 *
 * STUB — fingerprint discovery needed via disassemble workflow — file placeholder only.
 *
 * Intent: strip the "Sent via Truecaller" watermark from outbound SMS messages
 * composed inside Truecaller's messaging surface. The watermark is a privacy leak
 * (advertises app usage to every recipient) and a spam vector.
 *
 * TODO: disassemble v26.10.6, grep for:
 *   - "Sent via Truecaller", "via Truecaller", string resource id references
 *   - SendMessageWorker / SmsCompose classes
 *   - concat/append sites on the outbound message body
 * Then either:
 *   a) Rewrite the string constant to empty
 *   b) Skip the append call entirely (return early before concat)
 */

package app.chirag127.morphe.patches.targets.truecaller

import app.morphe.patcher.patch.bytecodePatch
import app.chirag127.morphe.patches.shared.Constants.COMPATIBILITY_TRUECALLER

@Suppress("unused")
val truecallerWatermarkStripPatch = bytecodePatch(
    name = "Truecaller — strip outbound SMS watermark (STUB)",
    description = "STUB: remove the \"Sent via Truecaller\" trailer from outbound SMS. Fingerprints not yet discovered.",
    default = false,
) {
    compatibleWith(*COMPATIBILITY_TRUECALLER)

    execute {
        // TODO: implement once outbound-SMS-compose fingerprints are authored.
        // See file-level comment for grep starting points.
    }
}
