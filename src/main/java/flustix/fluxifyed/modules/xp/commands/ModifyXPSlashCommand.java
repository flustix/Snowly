package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Objects;

public class ModifyXPSlashCommand extends SlashCommand {
    public ModifyXPSlashCommand() {
        super("modifyxp", "Give/Remove XP to/from a user.");
        setPermissionLevel(PermissionLevel.MODERATOR);
        addOption(OptionType.USER, "user", "The user to give/remove XP.", true, false);
        addOption(OptionType.INTEGER, "amount", "The amount of XP to give/remove. (Use negative values to remove xp)", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        User user = Objects.requireNonNull(interaction.getOption("user")).getAsUser();
        int amount = Objects.requireNonNull(interaction.getOption("amount")).getAsInt();

        XPGuild guild = XP.getGuild(Objects.requireNonNull(interaction.getGuild()).getId());

        if (!Settings.getGuildSettings(interaction.getGuild().getId()).xpEnabled()) {
            SlashCommandUtils.replyEphemeral(interaction, ":x: XP is disabled on this server!");
            return;
        }

        guild.getUser(user.getId()).giveXP(amount);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(interaction.getUser().getAsTag(), null, interaction.getUser().getAvatarUrl())
                .setTitle("Gave XP to " + user.getAsTag())
                .addField(":1234: XP", amount + "", true)
                .setColor(Main.accentColor);

        SlashCommandUtils.reply(interaction, embed.build());
    }
}
