package flustix.fluxifyed.database.api.v1.components;

import flustix.fluxifyed.utils.ColorUtils;

import java.awt.*;

public class APIColor {
    public final String hex;
    public final int red;
    public final int green;
    public final int blue;
    public final int alpha;

    public APIColor(Color color) {
        if (color == null) {
            hex = "#ffffffff";
            red = 255;
            green = 255;
            blue = 255;
            alpha = 255;
        } else {
            hex = ColorUtils.rgbaToHex(color);
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
            alpha = color.getAlpha();
        }
    }
}
