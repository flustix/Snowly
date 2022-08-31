package flustix.fluxifyed.commands.utility;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.List;

public class ServerInfoSlashCommand extends SlashCommand {
    public ServerInfoSlashCommand() {
        super("serverinfo", "Get information about the server");
    }

    public void execute(SlashCommandInteraction interaction) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(interaction.getGuild().getName())
                .setThumbnail(interaction.getGuild().getIconUrl())
                .setColor(Main.accentColor);

        embed.addField(":1234: Server ID", interaction.getGuild().getId(), true);
        embed.addField(":crown: Server Owner", interaction.getGuild().getOwner().getAsMention(), true);
        embed.addField(":busts_in_silhouette: Member Count", interaction.getGuild().getMemberCount() + "", true);
        embed.addField(":clock1: Server Creation Date", interaction.getGuild().getTimeCreated().toString(), true);
        embed.addField(":books: Channels",
                "Total: " + interaction.getGuild().getChannels().size() + "\n"
                + ":pencil: Text Channels: " + interaction.getGuild().getTextChannels().size() + "\n"
                + ":sound: Voice Channels: " + interaction.getGuild().getVoiceChannels().size(),
                true);
        embed.addField(":moyai: Emojis", interaction.getGuild().getEmojis().size() + "", true);

        List<String> roles = new ArrayList<>();
        for (Role role : interaction.getGuild().getRoles()) {
            roles.add(role.getAsMention());
        }
        embed.addField(":scroll: Roles", String.join(", ", roles), false);

        SlashCommandUtils.reply(interaction, embed.build());
    }
}
