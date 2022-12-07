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

        float imageRatio = (float) image.getWidth() / image.getHeight();
        float targetRatio = (float) width / height;
        int targetWidth;
        int targetHeight;

        if (targetRatio > imageRatio) {
            targetWidth = width;
            targetHeight = (int) (width / imageRatio);
        } else {
            targetWidth = (int) (height * imageRatio);
            targetHeight = height;
        }

        int x = (width - targetWidth) / 2;
        int y = (height - targetHeight) / 2;

        graphics2D.drawImage(image, x, y, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
