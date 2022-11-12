package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ModifyXPSlashCommand extends SlashCommand {
    public ModifyXPSlashCommand() {
        super("modifyxp", "Give/Remove XP to/from a user.", true);
        addPermissions(Permission.ADMINISTRATOR);
        addOption(OptionType.USER, "user", "The user to give/remove XP.", true, false);
        addOption(OptionType.INTEGER, "amount", "The amount of XP to give/remove. (Use negative values to remove xp)", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userMapping = interaction.getOption("user");
        OptionMapping amountMapping = interaction.getOption("amount");
        if (userMapping == null || amountMapping == null) return;

        Guild g = interaction.getGuild();
        if (g == null) return;

        User user = userMapping.getAsUser();
        int amount = amountMapping.getAsInt();

        XPGuild guild = XP.getGuild(g.getId());

        if (!Settings.getGuildSettings(interaction.getGuild().getId()).moduleEnabled("xp")) {
            interaction.reply(":x: XP is disabled on this server!").queue();
            return;
        }

        guild.getUser(user.getId()).giveXP(amount);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(interaction.getUser().getAsTag(), null, interaction.getUser().getAvatarUrl())
                .setTitle("Gave XP to " + user.getAsTag())
                .addField(":1234: XP", amount + "", true)
                .setColor(Main.accentColor);

        interaction.replyEmbeds(embed.build()).queue();
    }
}
