package flustix.fluxifyed.utils.graphics;

import java.awt.*;

public class GraphicsUtils {
    public static void drawString(Graphics2D g2d, String str, int x, int y) {
        g2d.drawString(str, x, y + g2d.getFontMetrics().getHeight());
    }

    public static void drawCenteredString(Graphics2D g2d, String str, int x, int y) {
        int width = g2d.getFontMetrics().stringWidth(str);
        g2d.drawString(str, x - (width / 2), y + g2d.getFontMetrics().getHeight());
    }

    public static void drawRightAlignedString(Graphics2D g2d, String str, int x, int y) {
        int width = g2d.getFontMetrics().stringWidth(str);
        g2d.drawString(str, x - width, y + g2d.getFontMetrics().getHeight());
    }
}
