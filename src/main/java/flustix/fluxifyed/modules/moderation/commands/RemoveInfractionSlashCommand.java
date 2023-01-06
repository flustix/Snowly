package flustix.fluxifyed.modules.moderation.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.moderation.ModerationModule;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;


public class RemoveInfractionSlashCommand extends SlashCommand {
    public RemoveInfractionSlashCommand() {
        super("removeinfraction", "Removes an infraction from a user.", true);
        addPermissions(Permission.MODERATE_MEMBERS);
        addOption(OptionType.INTEGER, "id", "The ID of the infraction to remove.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping idMapping = interaction.getOption("id");
        if (idMapping == null) return;

        int id = idMapping.getAsInt();
        Guild guild = interaction.getGuild();
        if (guild == null) return;

        try {
            Infraction infraction = ModerationModule.getInfraction(id);
            if (infraction == null) {
                interaction.reply("Infraction not found.").setEphemeral(true).queue();
                return;
            }

            if (!infraction.getGuild().equals(guild.getId())) {
                interaction.reply("Infraction not found.").setEphemeral(true).queue();
                return;
            }

            infraction.removeFromDatabase();
            ModerationModule.getInfractions().remove(infraction);
            interaction.reply("Removed infraction #" + id + " from <@" + infraction.getUser() + ">.").setEphemeral(true).queue();
        } catch (Exception e) {
            e.printStackTrace();
            interaction.reply("An error occurred.").setEphemeral(true).queue();
        }
    }
}
