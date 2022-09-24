package flustix.fluxifyed.modules.xp.images;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.graphics.Alignment;
import flustix.fluxifyed.utils.graphics.GraphicsUtils;
import flustix.fluxifyed.utils.xp.XPUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class RankImage {
    public static File file = new File("rank.png");

    public static boolean create(String pfp, String username, String servericon, String servername, int xp, int rank, Color color) {
        try {
            if (color == null) color = new Color(Main.accentColor);

            BufferedImage image = new BufferedImage(1200, 460, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            Font font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/fonts/Lato-Bold.ttf"));

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            graphics.setColor(new Color(47, 49, 54));
            graphics.fillRoundRect(10, 10, 1180, 450, 5, 5);

            // profile picture
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(20, 20, 190, 190, 5, 5);
            graphics.drawImage(GraphicsUtils.drawRoundedRect(GraphicsUtils.scaleImage(ImageIO.read(new URL(pfp)), 170, 170), 8), 30, 30, null);

            // username
            graphics.fillRoundRect(220, 20, 760, 190, 5, 5);
            graphics.setFont(font.deriveFont(64f));
            graphics.setColor(color);
            GraphicsUtils.drawString(graphics, username, 230, 20, 64, 740, Alignment.LEFT);

            // server icon
            graphics.drawImage(GraphicsUtils.drawRoundedRect(GraphicsUtils.scaleImage(ImageIO.read(new URL(servericon)), 60, 60), 4), 230, 120, null);

            // server name
            graphics.setFont(font.deriveFont(32f));
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawString(graphics, servername, 300, 122, Alignment.LEFT);

            // rank
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(990, 20, 190, 190, 5, 5);
            graphics.setColor(color);
            GraphicsUtils.drawString(graphics, "Rank", 1085, 70, Alignment.CENTER);
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawString(graphics, "#" + rank, 1085, 110, Alignment.CENTER);


            int level = XPUtils.calculateLevel(xp);
            float nextLevel = XPUtils.calculateXP(level + 1);

            // xp bg
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(20, 220, 380, 180, 5, 5);
            graphics.fillRoundRect(410, 220, 380, 180, 5, 5);
            graphics.fillRoundRect(800, 220, 380, 180, 5, 5);

            //xp text
            graphics.setColor(color);
            GraphicsUtils.drawString(graphics, "Level", 210, 260, Alignment.CENTER);
            GraphicsUtils.drawString(graphics, "XP", 600, 260, Alignment.CENTER);
            GraphicsUtils.drawString(graphics, "XP Left", 990, 260, Alignment.CENTER);
            graphics.setColor(Color.WHITE);
            GraphicsUtils.drawString(graphics, level + "", 210, 300, Alignment.CENTER);
            GraphicsUtils.drawString(graphics, xp + "", 600, 300, Alignment.CENTER);
            GraphicsUtils.drawString(graphics, Math.round(nextLevel - xp) + "", 990, 300, Alignment.CENTER);

            // progressbar
            graphics.setColor(new Color(54, 57, 63));
            graphics.fillRoundRect(20, 410, 1160, 40, 5, 5);
            graphics.setColor(new Color(47, 49, 54));
            graphics.fillRoundRect(30, 420, 1140, 20, 5, 5);
            graphics.setColor(color);
            graphics.fillRoundRect(30, 420, (int) (1140 * (xp / nextLevel)), 20, 5, 5);

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
}
