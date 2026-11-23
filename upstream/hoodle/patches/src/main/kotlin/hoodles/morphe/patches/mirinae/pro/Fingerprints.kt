package hoodles.morphe.patches.mirinae.pro

import app.morphe.patcher.Fingerprint

object ShouldInterceptRequestFingerprint : Fingerprint(
    name = "shouldInterceptRequest",
    definingClass = "Lcom/pichillilorenzo/flutter_inappwebview/in_app_webview/InAppWebViewClient;",
    parameters = listOf("Landroid/webkit/WebView;", "Landroid/webkit/WebResourceRequest;")
)

object MainActivityOnCreateFingerprint : Fingerprint(
    classFingerprint = Fingerprint(strings = listOf("dart_entrypoint")),
    name = "onCreate",
    parameters = listOf("Landroid/os/Bundle;")
)