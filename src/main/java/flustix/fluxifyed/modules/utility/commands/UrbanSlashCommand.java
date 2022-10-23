package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.utility.utils.urban.UrbanUtils;
import flustix.fluxifyed.modules.utility.utils.urban.components.UrbanDefinition;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class UrbanSlashCommand extends SlashCommand {
    public UrbanSlashCommand() {
        super("urban", "Searches the Urban Dictionary for a definition of a word.");
        addOption(OptionType.STRING, "word", "The word to search for.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping wordMapping = interaction.getOption("word");
        if (wordMapping == null) return;

        String word = wordMapping.getAsString();

        UrbanDefinition definition = UrbanUtils.getDefinition(word);

        if (definition == null) {
            interaction.reply("No definition found for " + word + ".").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(definition.getWord())
                .setDescription(definition.getDefinition())
                .setColor(0x1b2936)
                .addField(":star: Example", definition.getExample(), false)
                .addField(":bust_in_silhouette: Author", definition.getAuthor(), true)
                .addField(":arrow_up: Thumbs Up", String.valueOf(definition.getThumbsUp()), true)
                .addField(":arrow_down: Thumbs Down", String.valueOf(definition.getThumbsDown()), true)
                .setFooter("Urban Dictionary");

        interaction.reply(new MessageCreateBuilder()
                .setEmbeds(embedBuilder.build())
                .addActionRow(Button.link(definition.getPermalink(), "View on Urban Dictionary"))
                .build()).queue();
    }
}
