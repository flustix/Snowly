package flustix.fluxifyed.utils.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsUtils {
    public static void drawString(Graphics2D g2d, String str, int x, int y, Alignment align) {
        int width = g2d.getFontMetrics().stringWidth(str);

        int finalX = switch (align) {
            case LEFT -> x;
            case CENTER -> x - (width / 2);
            case RIGHT -> x - width;
        };

        g2d.drawString(str, finalX, y + g2d.getFontMetrics().getHeight());
    }

    public static void drawString(Graphics2D g2d, String str, int x, int y, int size, int maxWidth, Alignment align) {
        g2d.setFont(g2d.getFont().deriveFont((float) size));
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

        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (image.getWidth() < image.getHeight()) {
            float factor = (float) width / image.getWidth();
            int newHeight = (int) (image.getHeight() * factor);
            int y = (height - newHeight) / 2;

            graphics2D.drawImage(image, 0, y, width, newHeight, null);
        } else {
            float factor = (float) height / image.getHeight();
            int newWidth = (int) (image.getWidth() * factor);
            int x = (width - newWidth) / 2;

            graphics2D.drawImage(image, x, 0, newWidth, height, null);
        }

        graphics2D.dispose();
        return scaledImage;
    }
}
