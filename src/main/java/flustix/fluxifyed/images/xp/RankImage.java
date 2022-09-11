package flustix.fluxifyed.images.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.graphics.GraphicsUtils;
import flustix.fluxifyed.utils.xp.XPUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class RankImage {
    public static File file = new File("rank.png");

    public static boolean create(String pfp, String username, String servericon, String servername, int xp) {
        try {
            BufferedImage image = new BufferedImage(1200, 500, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            Font font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/fonts/Lato-Bold.ttf"));

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            graphics.setColor(new Color(0, 0, 0, 255 / 4));
            graphics.fillRoundRect(10, 10 , 1180, 480, 24, 24);

            // profile picture
            graphics.drawImage(drawRoundedRect(scaleImage(ImageIO.read(new URL(pfp)), 170, 170), 8), 30, 30, null);

            // username
            graphics.setFont(font.deriveFont(64f));
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawString(graphics, username, 210, 20);

            // server icon
            graphics.drawImage(drawRoundedRect(scaleImage(ImageIO.read(new URL(servericon)), 60, 60), 4), 210, 120, null);

            // server name
            graphics.setFont(font.deriveFont(32f));
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawString(graphics, servername, 280, 122);

            // progressbar
            int level = XPUtils.calculateLevel(xp);
            float nextLevel = XPUtils.calculateXP( level + 1);
            graphics.setColor(new Color(0, 0, 0, 255 / 4));
            graphics.fillRoundRect(30, 450, 1140, 20, 5, 5);
            graphics.setColor(new Color(Main.accentColor));
            graphics.fillRoundRect(30, 450, (int) (1140 * (xp / nextLevel)), 20, 5, 5);

            // xp
            graphics.setColor(new Color(Main.accentColor));
            GraphicsUtils.drawCenteredString(graphics, "Level", 300, 260);
            GraphicsUtils.drawCenteredString(graphics, "XP", 600, 260);
            GraphicsUtils.drawCenteredString(graphics, "XP Left", 900, 260);
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawCenteredString(graphics, level + "", 300, 300);
            GraphicsUtils.drawCenteredString(graphics, xp + "", 600, 300);
            GraphicsUtils.drawCenteredString(graphics, Math.round(nextLevel - xp) + "", 900, 300);

            graphics.dispose();
            ImageIO.write(image, "png", file);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

//        ImagePlus imagePlus = new ImagePlus("test.png");
//        imagePlus.show();
    }

    static BufferedImage drawRoundedRect(BufferedImage image, int cornerRadius) {
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

    static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
