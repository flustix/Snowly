package flustix.fluxifyed.modules.reactionroles.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.reactionroles.ReactionRoles;
import flustix.fluxifyed.utils.presets.EmbedPresets;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ReactCreateSlashCommand extends SlashCommand {
    public ReactCreateSlashCommand() {
        super("reactcreate", "Create a reaction role message", true);
        addPermissions(Permission.MANAGE_SERVER);
        addOption(OptionType.STRING, "name", "The title for the message.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping nameMapping = interaction.getOption("name");
        if (nameMapping == null) return;

        interaction.replyEmbeds(EmbedPresets.loading.build()).queue((hook) -> {
            EmbedBuilder reactEmbed = new EmbedBuilder()
                    .setTitle(nameMapping.getAsString())
                    .setColor(Main.accentColor)
                    .setDescription("React to this message to get a role!");

            interaction.getChannel().sendMessageEmbeds(reactEmbed.build()).queue((message) -> {
                JsonObject data = new JsonObject();
                data.addProperty("name", nameMapping.getAsString());
                data.add("roles", new JsonArray());
                ReactionRoles.addMessage(message.getId(), data.toString());

                hook.editOriginalEmbeds(new EmbedBuilder()
                        .setTitle("Finished!")
                        .setColor(Main.accentColor)
                        .build()).queue();
            });
        });
    }
}
