package hoodles.morphe.patches.shared.misc.signature

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

object SignatureSpoofApplicationCtorFingerprint : Fingerprint (
    definingClass = Constants.SPOOF_CLASS_SMALI_NAME,
    name = "<clinit>",
    filters = listOf(
        string("PACKAGE_NAME_PLACEHOLDER"),
        string("SIGNATURE_PLACEHOLDER")
    )
)