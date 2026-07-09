package hoodles.morphe.extension.shared;

import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@SuppressWarnings("NewApi")
public class Utils {
    public static String smaliClassToJava(String clazz) {
        return clazz.substring(1, clazz.length() - 1).replace('/', '.');
    }

    private static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");
    private static final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
    public static String toIso8601(Date date) {
        iso8601Format.setTimeZone(utcTZ);
        return iso8601Format.format(date);
    }

    public static WebResourceResponse getResponse(HttpURLConnection connection, String bodyOverride) throws IOException {
        return new WebResourceResponse(
                "application/javascript",
                "UTF-8",
                connection.getResponseCode(),
                connection.getResponseMessage(),
                getHeaders(connection),
                new ByteArrayInputStream(bodyOverride.getBytes(StandardCharsets.UTF_8))
        );
    }

    private static Map<String, String> getHeaders(HttpURLConnection connection) {
        Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                headers.put(entry.getKey(), entry.getValue().get(0));
            }
        }

        return headers;
    }
}
