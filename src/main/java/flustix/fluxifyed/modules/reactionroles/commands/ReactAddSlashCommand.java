package flustix.fluxifyed.modules.reactionroles.commands;

import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.modules.reactionroles.ReactionRoles;
import flustix.fluxifyed.modules.reactionroles.components.ReactionRoleMessage;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ReactAddSlashCommand extends SlashCommand {
    public ReactAddSlashCommand() {
        super("reactadd", "Add a reaction role to a message");
        setPermissionLevel(PermissionLevel.MODERATOR);
        addOption(OptionType.STRING, "messageid", "The ID of the message to add the reaction role to.", true, false);
        addOption(OptionType.STRING, "emoji", "The emoji to react with.", true, false);
        addOption(OptionType.ROLE, "role", "The role to give.", true, false);
        addOption(OptionType.STRING, "name", "The name of the reaction role.", true, false);
        addOption(OptionType.STRING, "description", "The description of the reaction role.", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        ReactionRoleMessage message = ReactionRoles.getMessage(interaction.getOption("messageid").getAsString());

        if (message == null) {
            interaction.reply("That message doesn't exist!").setEphemeral(true).queue();
            return;
        }

        RichCustomEmoji emoji;
        try {
            String emojiName = interaction.getOption("emoji").getAsString().split(":")[1];
            emoji = interaction.getGuild().getEmojisByName(emojiName, false).get(0);
        } catch (Exception e) {
            interaction.reply("That emoji doesn't exist / isn't from this server!").setEphemeral(true).queue();
            e.printStackTrace();
            return;
        }

        message.addRole(emoji.getAsMention(), interaction.getOption("role").getAsRole().getId(), interaction.getOption("name").getAsString(), interaction.getOption("description").getAsString());
        boolean successful = message.update(interaction);

        if (successful)
            interaction.reply("Added the reaction role!").setEphemeral(true).queue();
    }
}
