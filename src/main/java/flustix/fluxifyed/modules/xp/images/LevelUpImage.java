package flustix.fluxifyed.modules.xp.images;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.graphics.Alignment;
import flustix.fluxifyed.utils.graphics.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class LevelUpImage {
    public static final File file = new File("levelup.png");

    public static boolean create(String pfp, String username, int level) {
        try {
            int width = 800;
            int height = 200;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            InputStream stream = Main.class.getResourceAsStream("/fonts/Lato-Bold.ttf");

            if (stream == null) {
                Main.LOGGER.error("Font not found!");
                return false;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            graphics.setColor(new Color(47, 49, 54));
            graphics.fillRoundRect(10, 10, width - 20, height - 20, 5, 5);

            // pfp
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(20, 20, 160, 160, 5, 5);
            graphics.drawImage(GraphicsUtils.drawRoundedRect(GraphicsUtils.scaleImage(ImageIO.read(new URL(pfp)), 140, 140), 5), 30, 30, null);

            // text
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(190, 20, 590, 160, 5, 5);
            graphics.setColor(Color.WHITE);
            graphics.setFont(font.deriveFont(50f));
            GraphicsUtils.drawString(graphics, username, 200, 20, 50, 570, Alignment.LEFT);
            graphics.setFont(font.deriveFont(30f));
            GraphicsUtils.drawString(graphics, "Leveled up to level " + level, 200, 110, Alignment.LEFT);

            graphics.dispose();
            ImageIO.write(image, "png", file);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
