package app.morphe.extension.tiktok.feedfilter;

import app.morphe.extension.tiktok.settings.Settings;
import com.ss.android.ugc.aweme.feed.model.Aweme;

import java.util.List;

public class ImageVideoFilter implements IFilter {
    @Override
    public boolean getEnabled() {
        return Settings.HIDE_IMAGE.get();
    }

    @Override
    public boolean getFiltered(Aweme item) {
        // TikTok 43.6.2: Aweme no longer exposes isImage()/isPhotoMode().
        List imageInfos = item.getImageInfos();
        boolean isImage = imageInfos != null && !imageInfos.isEmpty();
        boolean isPhotoMode =
            item.getPhotoModeImageInfo() != null || item.getPhotoModeTextInfo() != null;
        return isImage || isPhotoMode;
    }
}

