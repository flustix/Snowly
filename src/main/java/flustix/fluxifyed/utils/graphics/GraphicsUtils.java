package flustix.fluxifyed.utils.graphics;

import flustix.fluxifyed.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsUtils {
    public static void drawString(Graphics2D g2d, String str, int x, int y, Alignment align) {
        int width = g2d.getFontMetrics().stringWidth(str);
        int finalX = 0;

        switch (align) {
            case LEFT:
                finalX = x;
                break;
            case CENTER:
                finalX = x - (width / 2);
                break;
            case RIGHT:
                finalX = x - width;
                break;
        }

        g2d.drawString(str, finalX, y + g2d.getFontMetrics().getHeight());
    }

    public static void drawString(Graphics2D g2d, String str, int x, int y, int size, int maxWidth, Alignment align) {
        int width = g2d.getFontMetrics().stringWidth(str);

        if (width > maxWidth) {
            float newSize = size * (maxWidth / (float) width);
            g2d.setFont(g2d.getFont().deriveFont(newSize));

            y += (size - newSize) / 2;
        }

        drawString(g2d, str, x, y, align);
    }

    public static BufferedImage drawRoundedRect(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
