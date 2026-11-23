package hoodles.morphe.patches.nomone.shared.tamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val disableAntiTamperPatch = bytecodePatch {

    execute {
        IsValidSignatureFingerprint.method.returnEarly(true)
        IsCrackedFingerprint.method.returnEarly(false)
    }
}