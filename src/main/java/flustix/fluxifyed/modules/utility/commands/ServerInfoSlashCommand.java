package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.temporal.ChronoField;

public class ServerInfoSlashCommand extends SlashCommand {
    public ServerInfoSlashCommand() {
        super("serverinfo", "Get information about the server", true);
    }

    public void execute(SlashCommandInteraction interaction) {
        Guild guild = interaction.getGuild();
        if (guild == null) return;

        Member owner = guild.getOwner();
        if (owner == null) {
            Main.LOGGER.warn("Guild Member intent is not enabled!");
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(guild.getName())
                .setThumbnail(interaction.getGuild().getIconUrl())
                .setColor(Main.accentColor);

        embed.addField(":1234: Server ID", interaction.getGuild().getId(), true);
        embed.addField(":crown: Server Owner", owner.getAsMention(), true);
        embed.addField(":busts_in_silhouette: Member Count", interaction.getGuild().getMemberCount() + "", true);
        embed.addField(":clock1: Server Creation Date", "<t:" + interaction.getGuild().getTimeCreated().getLong(ChronoField.INSTANT_SECONDS) + ":f>", true);
        embed.addField(":books: Channels",
                "Total: " + interaction.getGuild().getChannels().size() + "\n"
                        + ":pencil: Text Channels: " + interaction.getGuild().getTextChannels().size() + "\n"
                        + ":sound: Voice Channels: " + interaction.getGuild().getVoiceChannels().size(),
                true);
        embed.addField(":moyai: Emojis", interaction.getGuild().getEmojis().size() + "", true);
        embed.addField(":scroll: Roles", interaction.getGuild().getRoles().size() + "", true);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
