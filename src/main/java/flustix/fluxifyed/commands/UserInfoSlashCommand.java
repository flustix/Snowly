package flustix.fluxifyed.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.List;

public class UserInfoSlashCommand extends SlashCommand {
    public UserInfoSlashCommand() {
        super("userinfo", "Get information about a user");
        this.addOption(OptionType.USER, "user", "The user to get information about", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping option = interaction.getOption("user");
        User u;
        if (option == null) {
            u = interaction.getUser();
        } else {
            u = option.getAsUser();
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(u.getName())
                .setThumbnail(u.getEffectiveAvatarUrl())
                .setColor(Main.accentColor);

        embed.addField(":abc: Tag",u.getAsTag(), true);
        embed.addField(":1234: ID", u.getId(), true);
        embed.addField(":clock1: Creation Date", "<t:" + u.getTimeCreated().toEpochSecond() + ":f>", true);

        Member m = interaction.getGuild().getMemberById(u.getId());

        if (m == null) {
            m = interaction.getGuild().retrieveMemberById(u.getId()).complete();
        }

        if(m != null) {
            List<String> roles = new ArrayList<>();
            for (Role role : m.getRoles()) {
                roles.add(role.getAsMention());
            }

            embed.addField(":clock1: Joined at", "<t:" + m.getTimeJoined().toEpochSecond() + ":f>", true);
            embed.addField(":scroll: Roles", String.join(", ", roles), false);
        }

        SlashCommandUtils.reply(interaction, embed.build());
    }
}
