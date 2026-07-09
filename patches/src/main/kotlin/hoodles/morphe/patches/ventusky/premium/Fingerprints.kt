package hoodles.morphe.patches.ventusky.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object SignatureCheckFingerprint : Fingerprint(
    strings = listOf("user_premium_mode", "user_premium", "SHA-256")
)

object PremiumCodeCtorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    strings = listOf("FREE", "PREMIUM", "PREMIUM_PLUS"),
    filters = listOf(
        string("PREMIUM"),
        opcode(Opcode.SPUT_OBJECT)
    ),
    custom = { _, classDef -> !classDef.contains("UserPremiumVersion") }
)

object GetPlanStatusFingerprint : Fingerprint(
    classFingerprint = SignatureCheckFingerprint,
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/String;", "Ljava/lang/String;")
)