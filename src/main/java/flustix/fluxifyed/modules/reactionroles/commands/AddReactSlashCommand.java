package flustix.fluxifyed.modules.reactionroles.commands;

import com.google.gson.JsonParser;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.reactionroles.ReactRoles;
import flustix.fluxifyed.modules.reactionroles.components.ReactMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class AddReactSlashCommand extends SlashCommand {
    public AddReactSlashCommand() {
        super("addreact", "Add a reaction role to a message.", true);
        addOption(OptionType.STRING, "data", "The data of the message.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping dataMapping = interaction.getOption("data");
        if (dataMapping == null) {
            interaction.reply("You must provide the data of the message.").setEphemeral(true).queue();
            return;
        }

        Guild guild = interaction.getGuild();
        if (guild == null) {
            interaction.reply("This command can only be used in a guild.").setEphemeral(true).queue();
            return;
        }

        ReactMessage reactMessage = new ReactMessage(JsonParser.parseString(dataMapping.getAsString()).getAsJsonObject(), true);
        reactMessage.sendMessage(interaction.getChannel());
        interaction.reply("Added reaction role message " + reactMessage.messageid).setEphemeral(true).queue();
    }
}
