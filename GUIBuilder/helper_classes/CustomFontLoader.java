package helper_classes;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class CustomFontLoader {

    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            font = font.deriveFont(size);
            return font;
        } catch (FontFormatException | IOException e) {
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }
}