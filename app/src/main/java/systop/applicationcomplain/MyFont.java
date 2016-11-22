package systop.applicationcomplain;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

/**
 * Created by Fafour on 26/10/2559.
 */
public class MyFont  implements FontProvider {
    private static final String FONT_PATH = "/system/fonts/DroidSans.ttf";
    private static final String FONT_ALIAS = "my_font";

    public MyFont(){ FontFactory.register(FONT_PATH, FONT_ALIAS); }

    @Override
    public Font getFont(String fontname, String encoding, boolean embedded,
                        float size, int style, BaseColor color){

        return FontFactory.getFont(FONT_ALIAS, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED, size, style, color);
    }

    @Override
    public boolean isRegistered(String name) { return name.equals( FONT_ALIAS ); }
}