package app.morphe.patches.tiktok.interaction.offlinevideos

import app.morphe.patcher.Fingerprint

internal object OfflineModeSheetOptionsFingerprint : Fingerprint(
    returnType = "V",
    custom = { method, classDef ->
        classDef.endsWith("/OfflineModeSheetPageAssem;") &&
            method.name == "<clinit>" &&
            method.parameterTypes.isEmpty()
    },
)

internal object OfflineModeListConstructorFingerprint : Fingerprint(
    returnType = "V",
    custom = { method, classDef ->
        classDef.endsWith("/OfflineModeListVM;") &&
            method.name == "<init>" &&
            method.parameterTypes.isEmpty()
    },
)

internal object OfflineModeOptionConfigFingerprint : Fingerprint(
    returnType = "V",
    custom = { method, classDef ->
        classDef.type == "LX/0seq;" &&
            method.name == "<clinit>" &&
            method.parameterTypes.isEmpty()
    },
)

internal object OfflineModeOptionEnumFingerprint : Fingerprint(
    returnType = "V",
    custom = { method, classDef ->
        classDef.type == "LX/0sek;" &&
            method.name == "<clinit>" &&
            method.parameterTypes.isEmpty()
    },
)
