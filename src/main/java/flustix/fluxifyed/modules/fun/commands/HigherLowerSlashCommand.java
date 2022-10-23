package flustix.fluxifyed.modules.fun.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.fun.utils.higherlower.HigherLowerUtils;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class HigherLowerSlashCommand extends SlashCommand {
    public HigherLowerSlashCommand() {
        super("higherlower", "Play a game of higher or lower");
    }

    public void execute(SlashCommandInteraction interaction) {
        InteractionHook hook = interaction.reply("Setting up...").complete();
        HigherLowerUtils.startGame(hook);
    }
}
