package hoodles.morphe.extension.hypertrophy.premium;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.net.HttpURLConnection;

import hoodles.morphe.extension.shared.Utils;
import hoodles.morphe.extension.shared.requests.Requester;

@SuppressWarnings("unused")
public class EnablePremiumPatch {
    private static final String ENDPOINT = "/api/user/subscriptions";
    private static final String RESPONSE = "{\"activeSubscriptions\":[{\"access\":[\"training\"],\"platform\":\"android\"}],\"consumedIaps\":[{\"access\":[\"training\"]}],\"stripeIds\":[]}";

    public static WebResourceResponse patchSubscriptionRequest(WebResourceRequest request) {
        String path = request.getUrl().getPath();
        if (path != null && path.equals(ENDPOINT)) {
            HttpURLConnection connection = null;
            try {
                connection = Requester.getConnectionFromRequest(request);
                connection.connect();

                return Utils.getResponse(connection, RESPONSE);
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
        }

        return null;
    }
}