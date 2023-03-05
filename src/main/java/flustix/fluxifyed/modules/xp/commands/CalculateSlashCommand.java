package flustix.fluxifyed.modules.xp.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import flustix.fluxifyed.modules.xp.XP;
import flustix.fluxifyed.modules.xp.components.XPGuild;
import flustix.fluxifyed.modules.xp.components.XPRole;
import flustix.fluxifyed.modules.xp.components.XPUser;
import flustix.fluxifyed.modules.xp.utils.XPUtils;
import flustix.fluxifyed.settings.GuildSettings;
import flustix.fluxifyed.settings.Settings;
import flustix.fluxifyed.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.List;

public class CalculateSlashCommand extends SlashCommand {
    public CalculateSlashCommand() {
        super("calculate", true);
        addOption(OptionType.INTEGER, "level", "The level you want to reach.", false, true);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping levelMapping = interaction.getOption("level");
        int level;

        Guild g = interaction.getGuild();
        Member m = interaction.getMember();
        if (g == null || m == null) return;

        GuildSettings guildSettings = Settings.getGuildSettings(g.getId());
        if (!guildSettings.getBoolean("xp.enabled", true)) {
            interaction.reply("XP is disabled on this server.").setEphemeral(true).queue();
            return;
        }

        XPGuild guild = XP.getGuild(g.getId());
        XPUser user = guild.getUser(m.getId());

        if (levelMapping == null) {
            // try getting the next level reward role
            List<XPRole> next = new ArrayList<>(guild.getLevelRoles().stream().filter(r -> r.getValue() > user.getLevel()).toList());
            next.sort((a, b) -> Float.compare(a.getValue(), b.getValue()));

            if (next.size() > 0) {
                level = (int)next.get(0).getValue();
            } else {
                interaction.reply("There is no next reward role! Please specify a level.").setEphemeral(true).queue();
                return;
            }
        } else {
            level = levelMapping.getAsInt();
        }

        if (level < 1) {
            interaction.reply("The level must be at least 1!").setEphemeral(true).queue();
            return;
        }

        if (level < user.getLevel()) {
            interaction.reply("You are already at level " + user.getLevel() + "!").setEphemeral(true).queue();
            return;
        }

        long xpToLevel = XPUtils.calculateXP(level + 1, guildSettings.getString("xp.levelMode", "default"));
        long xpLeft = xpToLevel - user.getXP();

        int rngMin = guildSettings.getInt("xp.randomMin", 10);
        int rngMax = guildSettings.getInt("xp.randomMax", 20);
        int cooldown = guildSettings.getInt("xp.cooldown", 60);

        int messagesMin = (int) Math.ceil((double) xpLeft / rngMax);
        int messagesMax = (int) Math.ceil((double) xpLeft / rngMin);
        int messagesAvg = (int) Math.ceil((double) xpLeft / ((rngMin + rngMax) / 2f));

        String timeNeeded = FormatUtils.timeToString(((long) messagesAvg * cooldown) * 1000);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Level " + level)
                .setColor(Colors.ACCENT)
                .addField("Current XP", FormatUtils.formatNumber(user.getXP()), true)
                .addField("Needed XP", FormatUtils.formatNumber(xpToLevel), true)
                .addField("XP left", FormatUtils.formatNumber(xpLeft), true)
                .addField("XP per message", rngMin + "-" + rngMax, true)
                .addField("Messages needed", FormatUtils.formatNumber(messagesMin) + "-" + FormatUtils.formatNumber(messagesMax) + " (" + FormatUtils.formatNumber(messagesAvg) + " avg.)", true)
                .addField("Time needed", timeNeeded, true);

        interaction.replyEmbeds(embed.build()).queue();
    }

    public List<Command.Choice> handleAutocomplete(CommandAutoCompleteInteractionEvent event, String option, String input) {
        ArrayList<Command.Choice> choices = new ArrayList<>();

        Guild g = event.getGuild();
        if (g == null) return choices;

        XPGuild guild = XP.getGuild(g.getId());

        for (XPRole role : guild.getLevelRoles()) {
            choices.add(new Command.Choice("Level " + role.getValue(), role.getValue()));
        }

        return choices;
    }
}
