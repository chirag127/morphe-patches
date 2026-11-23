package hoodles.morphe.extension.goodreads.ads;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.util.List;

@SuppressWarnings("unused")
public class DisableAdsPatch {
    private static final List<String> blockedClassNames = List.of("AdspBagsAdSection", "BannerAdSection");
    private static final List<String> blockedDomains = List.of("securepubads");
    private static final WebResourceResponse emptyResponse = new WebResourceResponse(null, null, null);

    public static boolean shouldBlockSection(Object section) {
        String simpleName = section.getClass().getSimpleName();
        return blockedClassNames.contains(simpleName);
    }

    public static WebResourceResponse getBlockedRequest(WebResourceRequest request) {
        String url = request.getUrl().toString();
        for (String domain : blockedDomains) {
            if (url.contains(domain)) return emptyResponse;
        }

        return null;
    }
}