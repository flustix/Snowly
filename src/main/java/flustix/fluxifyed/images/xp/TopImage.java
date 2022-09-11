package flustix.fluxifyed.images.xp;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.graphics.GraphicsUtils;
import flustix.fluxifyed.xp.types.XPGuild;
import flustix.fluxifyed.xp.types.XPUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TopImage {
    public static File file = new File("top.png");

    public static boolean create(XPGuild guild) {
        try {
            int length = guild.getTop().size();

            if (length > 10) length = 10;

            int width = 1000;
            int height = (length * 90) + 20;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            Font font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/fonts/Lato-Bold.ttf"));

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background
            graphics.setColor(new Color(47, 49, 54));
            graphics.fillRoundRect(10, 10, width - 20, height - 10, 5, 5);

            for (int i = 0; i < length; i++) {
                XPUser user = guild.getTop().get(i);

                graphics.setColor(new Color(54, 57, 63));
                graphics.fillRoundRect(20, 20 + (90 * i), width - 40, 80, 5, 5);
                graphics.setColor(new Color(getRankColor(i + 1)));
                graphics.fillRoundRect(20, 20 + (90 * i), 80, 80, 5, 5);
                graphics.setColor(Color.WHITE);
                graphics.setFont(font.deriveFont(32f));
                GraphicsUtils.drawCenteredString(graphics, "#" + (i + 1), 60, 32 + (90 * i));

                Guild g = Main.getShards().get(0).getGuildById(guild.getID());

                try {
                    Member member = g.getMemberById(user.getID());
                    if (member == null)
                        member = g.retrieveMemberById(user.getID()).complete();

                    GraphicsUtils.drawString(graphics, member.getUser().getName(), 120, 32 + (90 * i));
                    graphics.setColor(Color.gray);
                    GraphicsUtils.drawString(graphics, "#" + member.getUser().getDiscriminator(), 120 + graphics.getFontMetrics().stringWidth(member.getUser().getName()), 32 + (90 * i));
                } catch (Exception e) {
                    GraphicsUtils.drawString(graphics, "Not Found", 120, 32 + (90 * i));
                }

                graphics.setColor(Color.WHITE);
                GraphicsUtils.drawRightAlignedString(graphics, user.getXP() + "XP", width - 30, 32 + (90 * i));
            }

            graphics.dispose();
            ImageIO.write(image, "png", file);

            return true;
        } catch (Exception e) {
            Main.LOGGER.error("An error occurred while creating the top image!", e);
            return false;
        }
    }

    static int getRankColor(int rank) {
        if (rank == 1) return 0xFFD700;
        if (rank == 2) return 0xC0C0C0;
        if (rank == 3) return 0xCD7F32;
        return 0x202225;
    }
}
