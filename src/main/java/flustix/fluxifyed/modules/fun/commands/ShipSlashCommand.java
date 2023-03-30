package flustix.fluxifyed.modules.fun.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ShipSlashCommand extends SlashCommand {
    public ShipSlashCommand() {
        super("ship", true);
        addOption(OptionType.USER, "user1", "The user to ship with", true, false);
        addOption(OptionType.USER, "user2", "The second user to ship with", false, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping user1 = interaction.getOption("user1");
        OptionMapping user2 = interaction.getOption("user2");

        User first;
        User second;

        if (user1 == null) {
            interaction.reply("Something went *very* wrong!").setEphemeral(true).queue();
            return;
        }

        if (user2 == null) {
            first = interaction.getUser();
            second = user1.getAsUser();
        } else {
            first = user1.getAsUser();
            second = user2.getAsUser();
        }

        float ship = (float) (Math.random() * 100);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Ship Result")
                .setDescription("**" + first.getAsTag() + "** and **" + second.getAsTag() + "** are **" + String.format("%.2f", ship) + "%** compatible!")
                .setColor(Colors.ACCENT);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
