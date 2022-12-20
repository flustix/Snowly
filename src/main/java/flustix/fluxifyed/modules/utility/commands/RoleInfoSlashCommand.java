package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.constants.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.RoleIcon;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;

public class RoleInfoSlashCommand extends SlashCommand {
    public RoleInfoSlashCommand() {
        super("roleinfo", "Get information about a role", true);
        addOption(OptionType.ROLE, "role", "The role to get information about", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping roleMapping = interaction.getOption("role");

        if (roleMapping == null) {
            interaction.reply("You must specify a role!").setEphemeral(true).queue();
            return;
        }

        Role role = roleMapping.getAsRole();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(role.getName() + " (" + role.getId() + ")")
                .setColor(role.getColor() == null ? new Color(Colors.ACCENT) : role.getColor())
                .addField(":star: Displayed Separately", role.isHoisted() ? "Yes" : "No", true)
                .addField(":bell: Mentionable by everyone", role.isMentionable() ? "Yes" : "No", true)
                .addField(":tools: Managed", role.isManaged() ? "Yes" : "No", true);

        String permissions = role.getPermissions().stream()
                .map(Permission::getName)
                .reduce((permission1, permission2) -> permission1 + ", " + permission2)
                .orElse("None");

        embedBuilder.addField(":hammer: Permissions", permissions, false);

        RoleIcon roleIcon = role.getIcon();

        if (roleIcon != null) {
            embedBuilder.setThumbnail(roleIcon.getIconUrl());
        }

        interaction.replyEmbeds(embedBuilder.build()).queue();
    }
}
