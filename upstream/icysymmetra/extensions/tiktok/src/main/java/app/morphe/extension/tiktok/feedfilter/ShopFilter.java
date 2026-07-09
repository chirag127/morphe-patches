package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

public class ShopFilter implements IFilter {
    // Placeholder used by TikTok internal shop ads (see upstream feed filter).
    private static final String SHOP_INFO = "placeholder_product_id";

    @Override
    public boolean getEnabled() {
        return Settings.HIDE_SHOP.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        String shareUrl = item.getShareUrl();
        // Null-safety: only filter when we actually have a URL.
        return shareUrl != null && shareUrl.contains(SHOP_INFO);
    }
}

