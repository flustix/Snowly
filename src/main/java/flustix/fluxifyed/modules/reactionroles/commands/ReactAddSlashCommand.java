package flustix.fluxifyed.modules.reactionroles.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.reactionroles.ReactionRoles;
import flustix.fluxifyed.modules.reactionroles.components.ReactionRoleMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ReactAddSlashCommand extends SlashCommand {
    public ReactAddSlashCommand() {
        super("reactadd", "Add a reaction role to a message", true);
        addPermissions(Permission.MANAGE_SERVER);
        addOption(OptionType.STRING, "messageid", "The ID of the message to add the reaction role to.", true, false);
        addOption(OptionType.STRING, "emoji", "The emoji to react with.", true, false);
        addOption(OptionType.ROLE, "role", "The role to give.", true, false);
        addOption(OptionType.STRING, "name", "The name of the reaction role.", true, false);
        addOption(OptionType.STRING, "description", "The description of the reaction role.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping messageIdMapping = interaction.getOption("messageid");
        OptionMapping emojiMapping = interaction.getOption("emoji");
        OptionMapping roleMapping = interaction.getOption("role");
        OptionMapping nameMapping = interaction.getOption("name");
        OptionMapping descMapping = interaction.getOption("description");

        Guild guild = interaction.getGuild();
        if (guild == null) return;

        if (messageIdMapping == null || emojiMapping == null || roleMapping == null || nameMapping == null || descMapping == null) return;

        ReactionRoleMessage message = ReactionRoles.getMessage(messageIdMapping.getAsString());

        if (message == null) {
            interaction.reply("That message doesn't exist!").setEphemeral(true).queue();
            return;
        }

        RichCustomEmoji emoji;
        try {
            String emojiName = emojiMapping.getAsString().split(":")[1];
            emoji = guild.getEmojisByName(emojiName, false).get(0);
        } catch (Exception e) {
            interaction.reply("That emoji doesn't exist / isn't from this server!").setEphemeral(true).queue();
            e.printStackTrace();
            return;
        }

        message.addRole(emoji.getAsMention(), roleMapping.getAsRole().getId(), nameMapping.getAsString(), descMapping.getAsString());
        boolean successful = message.update(interaction);

        if (successful) interaction.reply("Added the reaction role!").setEphemeral(true).queue();
    }
}
