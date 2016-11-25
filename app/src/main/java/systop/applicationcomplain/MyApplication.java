package systop.applicationcomplain;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Fafour on 24/11/2559.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFont();
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/datafont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
