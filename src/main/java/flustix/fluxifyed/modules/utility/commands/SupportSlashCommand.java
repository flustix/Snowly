package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class SupportSlashCommand extends SlashCommand {
    public SupportSlashCommand() {
        super("support", "Gives you the link to the support server.");
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        MessageCreateBuilder message = new MessageCreateBuilder()
                .setContent("After you join the support server, ask your question in the #fluxifyed-support forum channel.")
                .addActionRow(Button.link("https://discord.gg/GaKKeWg", "Join the support server."));

        interaction.reply(message.build()).queue();
    }
}
