package flustix.fluxifyed.modules.utility.commands;

import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.utils.permissions.PermissionLevel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is absolute overkill. I expect this to kill the bot.
 */
public class GiveRoleEveryoneSlashCommand extends SlashCommand {
    public GiveRoleEveryoneSlashCommand() {
        super("giveroleeveryone", "gives a role to everyone (bot owner only)");
        setPermissionLevel(PermissionLevel.CREATOR);
        addOption(OptionType.ROLE, "role", "the role to give", true, false);
    }

    public void execute(SlashCommandInteraction interaction) {
        interaction.reply("Starting...").complete();

        try {
            interaction.getGuild()
                    .loadMembers()
                    .onSuccess(members -> {
                        AtomicInteger i = new AtomicInteger();
                        Message msg = interaction.getChannel().sendMessage("Giving " + members.size() + " members the role.").complete();

                        for (Member member : members) {
                            if (!member.getRoles().contains(interaction.getOption("role").getAsRole())) {
                                interaction.getGuild().addRoleToMember(member, interaction.getOption("role").getAsRole()).queue(done -> {
                                    i.getAndIncrement();

                                    if (i.get() % 10 == 0) {
                                        msg.editMessage("Giving " + members.size() + " members the role. (" + i + " done)").queue();
                                    }
                                });
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            interaction.getChannel().sendMessage("Something went wrong: " + e.getMessage()).complete();
        }
    }
}
