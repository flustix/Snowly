package flustix.fluxifyed.modules.utility.components;

import flustix.fluxifyed.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.ArrayList;
import java.util.List;

public class PollInstance {
    private final String question;
    private final String option1;
    private final String option2;
    private final List<String> voters = new ArrayList<>();

    private int option1Votes = 0;
    private int option2Votes = 0;

    public PollInstance(String question, String option1, String option2) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
    }

    public void vote(ButtonInteractionEvent event) {
        if (voters.contains(event.getUser().getId())) {
            event.reply("You have already voted!").setEphemeral(true).queue();
            return;
        }

        voters.add(event.getUser().getId());

        if (event.getComponentId().endsWith("1")) {
            option1Votes++;
        } else {
            option2Votes++;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(question)
                .setColor(Main.accentColor)
                .addField(option1,  option1Votes + " votes", true)
                .addField(option2, option2Votes + " votes", true);

        event.editMessageEmbeds(embed.build()).queue();
    }

    public void end(ButtonInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(question)
                .setColor(Main.accentColor)
                .addField(option1,  option1Votes + " votes", true)
                .addField(option2, option2Votes + " votes", true)
                .setFooter("This poll has ended!");

        MessageEditBuilder message = new MessageEditBuilder()
                .setEmbeds(embed.build());

        event.editMessage(message.build()).queue();
        event.getInteraction().getMessage().editMessageComponents().queue();
    }
}
