/*
 * Forked from:
 * https://github.com/ReVanced/revanced-patches/blob/377d4e15016296b45d809697f7f69bce74badd3a/extensions/tiktok/src/main/java/app/revanced/extension/tiktok/settings/preference/TogglePreference.java
 */

package app.morphe.extension.tiktok.settings.preference;

import android.content.Context;
import android.preference.SwitchPreference;
import android.view.View;

import app.morphe.extension.shared.settings.BooleanSetting;
import app.morphe.extension.tiktok.Utils;

@SuppressWarnings("deprecation")
public class TogglePreference extends SwitchPreference {

    public TogglePreference(Context context, String title, String summary, BooleanSetting setting) {
        super(context);
        setTitle(title);
        setSummary(summary);
        setKey(setting.key);
        setChecked(setting.get());
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        Utils.setTitleAndSummaryColor(view);
    }
}

