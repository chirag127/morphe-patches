package hoodles.morphe.extension.mirinae.pro;

import hoodles.morphe.extension.shared.Utils;
import hoodles.morphe.extension.shared.requests.Requester;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.net.HttpURLConnection;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class EnableProPatch {
    private static final Pattern JS_BUNDLE_PATTERN = Pattern.compile("/js/(explorer|default-lessons~wikinae)\\.[0-9a-f]+\\.js$", Pattern.CASE_INSENSITIVE);
    private static final Pattern GET_PRO_OR_STAFF_PATTERN = Pattern.compile("\\{(var \\w+;return\"pro\")");

    public static WebResourceResponse patchAppJavascript(WebResourceRequest request) {
        String url = request.getUrl().toString();

        if (!JS_BUNDLE_PATTERN.matcher(url).find()) {
            return null;
        }

        HttpURLConnection connection = null;
        try {
            connection = Requester.getConnectionFromRequest(request);
            connection.connect();

            String jsBody = Requester.parseString(connection);
            String modifiedBody = patch(jsBody);

            return Utils.getResponse(connection, modifiedBody);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private static String patch(String jsBody) {
        return GET_PRO_OR_STAFF_PATTERN.matcher(jsBody).replaceAll("{return true; $1");
    }
}
