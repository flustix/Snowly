package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class InviteSlashCommand extends SlashCommand {
    public InviteSlashCommand() {
        super("invite", "Get the invite link for the bot");
    }

    public void execute(SlashCommandInteraction interaction) {
        MessageCreateBuilder message = new MessageCreateBuilder()
                .setContent("Here you go!")
                .addActionRow(Button.link(Main.getBot().getInviteUrl(Main.getRequiredPermissions()), "Invite to guild..."));

        interaction.reply(message.build()).queue();
    }
}
