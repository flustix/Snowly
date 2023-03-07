package flustix.fluxifyed.image;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.ColorUtils;
import flustix.fluxifyed.utils.FileUtils;
import flustix.fluxifyed.utils.graphics.Alignment;
import flustix.fluxifyed.utils.graphics.GraphicsUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class ImageRenderer {
    public static boolean renderImage(RenderArgs args) {
        try {
            String data = FileUtils.getResourceString("/images/" + args.template + ".image");
            String[] lines = data.split(";");

            int width = 0;
            int height = 0;

            String firstLine = lines[0];
            if (firstLine.startsWith("size")) {
                int index = firstLine.indexOf("(");
                int index2 = firstLine.indexOf(")");
                String[] size = firstLine.substring(index + 1, index2).split(",");
                width = Integer.parseInt(size[0].strip());
                height = Integer.parseInt(size[1].strip());
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            InputStream stream = Main.class.getResourceAsStream("/fonts/Quicksand-SemiBold.ttf"); //TODO: make font configurable

            if (stream == null) {
                Main.LOGGER.error("Font not found!");
                return false;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setFont(font.deriveFont(24f));

            for (String line : Arrays.stream(lines).skip(1).toArray(String[]::new)) {
                if (!line.isBlank()) renderObject(line.strip(), args.data, graphics);
            }

            graphics.dispose();
            ImageIO.write(image, "png", new File(args.output));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void renderObject(String line, RenderData data, Graphics2D g2d) {
        int index = line.indexOf("(");
        int index2 = line.indexOf(")");

        String[] args = line.substring(index + 1, index2).split(",");
        String type = line.substring(0, index);

        for (int i = 0; i < args.length; i++)
            args[i] = args[i].strip();

        switch (type) {
            case "rect" -> renderRect(args, data, g2d);
            case "xpbar" -> renderXpProgress(args, data, g2d);
            case "image" -> renderImage(args, data, g2d);
            case "text" -> renderText(args, data, g2d);
        }
    }

    /**
     * rect(color, x, y, width, height, radius)
     */
    private static void renderRect(String[] args, RenderData data, Graphics2D g2d) {
        String fill = args.length > 0 ? args[0] : "#ffffff";
        int x = args.length > 1 ? tryParseInt(args[1], 0) : 0;
        int y = args.length > 2 ? tryParseInt(args[2], 0) : 0;
        int w = args.length > 3 ? tryParseInt(args[3], 0) : 0;
        int h = args.length > 4 ? tryParseInt(args[4], 0) : 0;
        int r = args.length > 5 ? tryParseInt(args[5], 0) : 0;

        Main.LOGGER.info("Rendering rect: " + fill + ", " + x + ", " + y + ", " + w + ", " + h + ", " + r);

        fill = replaceVars(fill, data);

        g2d.setColor(ColorUtils.hexToRGBA(fill));
        g2d.fillRoundRect(x, y, w, h, r, r);
    }

    /**
     * xpbar(color, x, y, width, height, radius)
     */
    private static void renderXpProgress(String[] args, RenderData data, Graphics2D g2d) {
        String fill = args.length > 0 ? args[0] : "#ffffff";
        int x = args.length > 1 ? tryParseInt(args[1], 0) : 0;
        int y = args.length > 2 ? tryParseInt(args[2], 0) : 0;
        int width = args.length > 3 ? tryParseInt(args[3], 0) : 0;
        int height = args.length > 4 ? tryParseInt(args[4], 0) : 0;
        int radius = args.length > 5 ? tryParseInt(args[5], 0) : 0;

        fill = replaceVars(fill, data);

        float progress = Float.parseFloat(replaceVars("{xp.progress}", data));
        width = (int) (width * (progress / 100f));

        g2d.setColor(ColorUtils.hexToRGBA(fill));
        g2d.fillRoundRect(x, y, width, height, radius, radius);
    }

    /**
     * image(url, x, y, width, height, radius)
     */
    private static void renderImage(String[] args, RenderData data, Graphics2D g2d) {
        String url = args.length > 0 ? args[0] : "";
        int x = args.length > 1 ? tryParseInt(args[1], 0) : 0;
        int y = args.length > 2 ? tryParseInt(args[2], 0) : 0;
        int width = args.length > 3 ? tryParseInt(args[3], 0) : 0;
        int height = args.length > 4 ? tryParseInt(args[4], 0) : 0;
        int radius = args.length > 5 ? tryParseInt(args[5], 0) : 0;

        url = replaceVars(url, data);

        try {
            BufferedImage image = ImageIO.read(new URL(url));
            BufferedImage scaledImage = GraphicsUtils.scaleImage(image, width, height);
            BufferedImage roundedImage = GraphicsUtils.drawRoundedRect(scaledImage, radius);
            g2d.drawImage(roundedImage, x, y, width, height, null);
        } catch (Exception ignored) {
        }
    }

    /**
     * text(text, x, y, color, size, align, width)
     */
    private static void renderText(String[] args, RenderData data, Graphics2D g2d) {
        String text = args.length > 0 ? args[0] : "";
        int x = args.length > 1 ? tryParseInt(args[1], 0) : 0;
        int y = args.length > 2 ? tryParseInt(args[2], 0) : 0;
        String color = args.length > 3 ? args[3] : "#ffffff";
        int size = args.length > 4 ? tryParseInt(args[4], 24) : 24;
        String align = args.length > 5 ? args[5] : "left";
        int width = args.length > 6 ? tryParseInt(args[6], 1000) : 1000;

        text = replaceVars(text, data);
        color = replaceVars(color, data);

        Alignment alignment = Alignment.LEFT;
        switch (align) {
            case "center" -> alignment = Alignment.CENTER;
            case "right" -> alignment = Alignment.RIGHT;
        }

        g2d.setColor(ColorUtils.hexToRGBA(color));
        GraphicsUtils.drawString(g2d, text, x, y, size, width, alignment);
    }

    private static String replaceVars(String s, RenderData data) {
        while (!data.loaded) {
            try {
                Thread.sleep(100); // wait for data to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (String key : data.getKeys().keySet()) {
            String replacement = data.getKeys().get(key);
            if (replacement == null) replacement = "";
            s = s.replace("{" + key + "}", replacement);
        }

        return s;
    }

    private static int tryParseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
