package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserInfoSlashCommand extends SlashCommand {
    public UserInfoSlashCommand() {
        super("userinfo", "Get information about a user", true);
        this.addOption(OptionType.USER, "user", "The user to get information about", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping option = interaction.getOption("user");
        Member member;
        if (option == null) member = interaction.getMember();
        else member = option.getAsMember();

        if (member == null) {
            Main.LOGGER.warn("Guild Member intent is disabled!");
            return;
        }

        String title;
        if (member.getEffectiveName().equals(member.getUser().getName())) title = member.getUser().getAsTag();
        else title = member.getEffectiveName() + " (" + member.getUser().getAsTag() + ")";

        Color color = member.getColor();
        if (color == null) color = new Color(Colors.ACCENT);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setColor(color);

        embed.addField(":1234: ID", member.getUser().getId(), true);
        embed.addField(":clock1: Creation Date", "<t:" + member.getUser().getTimeCreated().toEpochSecond() + ":f>", true);
        embed.addField(":clock1: Joined at", "<t:" + member.getTimeJoined().toEpochSecond() + ":f>", true);

        List<String> roles = new ArrayList<>();
        for (Role role : member.getRoles()) {
            roles.add(role.getAsMention());
        }
        String rolesString = String.join(", ", roles);
        if (rolesString.isEmpty()) rolesString = "None";

        while (rolesString.length() > 1024) {
            rolesString = rolesString.substring(0, rolesString.lastIndexOf(","));
        }

        embed.addField(":scroll: Roles", rolesString, false);

        User.Profile profile = member.getUser().retrieveProfile().complete();

        if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=2048");

        interaction.replyEmbeds(embed.build()).queue();
    }
}
