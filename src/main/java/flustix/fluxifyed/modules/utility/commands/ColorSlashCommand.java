package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.util.Objects;

public class ColorSlashCommand extends SlashCommand {
    public ColorSlashCommand() {
        super("color", "Get information about a color.");
        addOption(OptionType.STRING, "hex", "The color to get information about. (Leave empty for a random color)", false, false);
        addOption(OptionType.STRING, "rgb", "The color to get information about. (Leave empty for a random color)", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping hexOption = interaction.getOption("hex");
        OptionMapping rgbOption = interaction.getOption("rgb");

        Color color;

        if (hexOption == null && rgbOption == null) {
            // random color
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            color = new Color(r, g, b);
        } else {
            if (hexOption != null && rgbOption == null) {
                // hex color
                color = parseColor(hexOption.getAsString());
            } else {
                // rgb color (or hex if both are specified)
                color = parseColor(Objects.requireNonNullElse(hexOption, rgbOption).getAsString());
            }
        }

        if (color == null) {
            interaction.reply("Invalid color!").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setFooter("requested by " + interaction.getUser().getAsTag(), interaction.getUser().getEffectiveAvatarUrl())
                .addField("Hex", String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()), true)
                .addField("RGB", String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue()), true)
                .addField("RGB%", String.format("%d%%, %d%%, %d%%", (int) (color.getRed() / 2.55), (int) (color.getGreen() / 2.55), (int) (color.getBlue() / 2.55)), true);

        interaction.replyEmbeds(embed.build()).queue();
    }

    private Color parseColor(String color) {
        if (color.startsWith("#")) {
            // hex color
            String hex = color.substring(1);

            if (hex.length() == 3) {
                // #RGB
                int r = Integer.parseInt(hex.substring(0, 1), 16);
                int g = Integer.parseInt(hex.substring(1, 2), 16);
                int b = Integer.parseInt(hex.substring(2, 3), 16);

                return new Color(r, g, b);
            } else if (hex.length() == 6) {
                // #RRGGBB
                int r = Integer.parseInt(hex.substring(0, 2), 16);
                int g = Integer.parseInt(hex.substring(2, 4), 16);
                int b = Integer.parseInt(hex.substring(4, 6), 16);

                return new Color(r, g, b);
            } else {
                // invalid hex color
                return null;
            }
        } else {
            // rgb color
            String[] rgb = color.split(",");

            if (rgb.length != 3) {
                // invalid rgb color
                return null;
            }

            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);

            return new Color(r, g, b);
        }
    }
}
