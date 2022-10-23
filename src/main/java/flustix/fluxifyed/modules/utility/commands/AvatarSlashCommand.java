package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class AvatarSlashCommand extends SlashCommand {
    public AvatarSlashCommand() {
        super("avatar", "Get the avatar of a user");
        addOption(OptionType.USER, "user", "The user to get the avatar of", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userMapping = interaction.getOption("user");

        Member member;

        if (userMapping == null) {
            member = interaction.getMember();
        } else {
            member = userMapping.getAsMember();
        }

        if (member == null) {
            Main.LOGGER.warn("Guild Member intent is not enabled!");
            return;
        }

        String name = member.getEffectiveName();

        if (name.endsWith("s") || name.endsWith("x")) {
            name += "'";
        } else {
            name += "'s";
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(name + " avatar")
                .setImage(member.getUser().getEffectiveAvatarUrl() + "?size=2048")
                .setColor(member.getColor());

        interaction.replyEmbeds(embed.build()).queue();
    }
}
