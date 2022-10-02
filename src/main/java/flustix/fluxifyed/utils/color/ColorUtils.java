package flustix.fluxifyed.utils.color;

import java.awt.*;

public class ColorUtils {
    public static Color hexToRGBA(String colorStr) {
        if (colorStr.startsWith("#"))
            colorStr = colorStr.substring(1);

        if (colorStr.length() == 3) {
            String r = colorStr.substring(0, 1);
            String g = colorStr.substring(1, 2);
            String b = colorStr.substring(2, 3);

            colorStr = r + r + g + g + b + b;
        }

        if (colorStr.length() == 4) {
            String r = colorStr.substring(0, 1);
            String g = colorStr.substring(1, 2);
            String b = colorStr.substring(2, 3);
            String a = colorStr.substring(3, 4);

            colorStr = r + r + g + g + b + b + a + a;
        }

        if (colorStr.length() == 6)
            colorStr += "ff";

        return new Color(
                Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16),
                Integer.valueOf(colorStr.substring(6, 8), 16)
        );
    }
}
