package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserInfoSlashCommand extends SlashCommand {
    public UserInfoSlashCommand() {
        super("userinfo", "Get information about a user");
        this.addOption(OptionType.USER, "user", "The user to get information about", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping option = interaction.getOption("user");
        Member m;
        if (option == null) m = interaction.getMember();
        else m = option.getAsMember();

        if (m == null) {
            Main.LOGGER.warn("Guild Member intent is disabled!");
            return;
        }

        String title;
        if (m.getEffectiveName().equals(m.getUser().getName())) title = m.getUser().getAsTag();
        else title = m.getEffectiveName() + " (" + m.getUser().getAsTag() + ")";

        Color color = m.getColor();
        if (color == null) color = new Color(Main.accentColor);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setThumbnail(m.getEffectiveAvatarUrl())
                .setColor(color);

        embed.addField(":1234: ID", m.getUser().getId(), true);
        embed.addField(":clock1: Creation Date", "<t:" + m.getUser().getTimeCreated().toEpochSecond() + ":f>", true);
        embed.addField(":clock1: Joined at", "<t:" + m.getTimeJoined().toEpochSecond() + ":f>", true);

        List<String> roles = new ArrayList<>();
        for (Role role : m.getRoles()) {
            roles.add(role.getAsMention());
        }
        String rolesString = String.join(", ", roles);
        if (rolesString.isEmpty()) rolesString = "None";

        while (rolesString.length() > 1024) {
            rolesString = rolesString.substring(0, rolesString.lastIndexOf(","));
        }

        embed.addField(":scroll: Roles", rolesString, false);

        SlashCommandUtils.reply(interaction, embed.build());
    }
}
