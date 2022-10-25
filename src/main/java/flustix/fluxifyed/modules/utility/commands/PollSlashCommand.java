package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.utility.components.PollInstance;
import flustix.fluxifyed.modules.utility.listeners.PollListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class PollSlashCommand extends SlashCommand {
    public PollSlashCommand() {
        super("poll", "Create a poll", true);
        addPermissions(Permission.MANAGE_CHANNEL);
        addOption(OptionType.STRING, "question", "The question to ask", true, false);
        addOption(OptionType.STRING, "option1", "The first option", true, false);
        addOption(OptionType.STRING, "option2", "The second option", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping questionMapping = interaction.getOption("question");
        OptionMapping option1Mapping = interaction.getOption("option1");
        OptionMapping option2Mapping = interaction.getOption("option2");

        if (questionMapping == null || option1Mapping == null || option2Mapping == null) return;

        String question = questionMapping.getAsString();
        String option1 = option1Mapping.getAsString();
        String option2 = option2Mapping.getAsString();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(question)
                .setColor(Main.accentColor)
                .addField(option1, "0 votes", true)
                .addField(option2, "0 votes", true);

        MessageCreateBuilder message = new MessageCreateBuilder()
                .setEmbeds(embed.build())
                .addActionRow(Button.primary("poll:1", option1))
                .addActionRow(Button.primary("poll:2", option2))
                .addActionRow(Button.danger("poll:end", "End Poll"));

        interaction.getChannel().sendMessage(message.build()).queue(msg -> {
            interaction.reply("Poll created!").setEphemeral(true).queue();
            PollListener.addPoll(msg.getId(), new PollInstance(question, option1, option2));
        });
    }
}
