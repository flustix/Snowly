package flustix.fluxifyed.modules.fun.utils.higherlower.components;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.fun.utils.higherlower.HigherLowerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.List;

public class HigherLowerGame {
    private final InteractionHook hook;
    private final List<HigherLowerRound> rounds;
    private int correct = 0;
    private int round;

    public HigherLowerGame(InteractionHook hook, List<HigherLowerRound> rounds) {
        this.hook = hook;
        this.rounds = rounds;
    }

    public void answer(ButtonInteractionEvent event) {
        String[] split = event.getComponentId().split(":");

        if (split[1].equals("answer")) {
            HigherLowerRound curRound = rounds.get(this.round);
            boolean correct = Integer.parseInt(split[2]) == curRound.getCorrectOption();
            EmbedBuilder embed;

            if (correct) {
                this.correct++;
                embed = new EmbedBuilder()
                        .setTitle("Higher or Lower")
                        .setDescription("Correct!\nYou got it right!")
                        .setColor(0x55FF55);
            } else {
                embed = new EmbedBuilder()
                        .setTitle("Higher or Lower")
                        .setDescription("Incorrect!\nYou got it wrong!")
                        .setColor(0xFF5555);
            }

            embed.addField(curRound.getOption1().getName(), curRound.getOption1().getValue() + " Searches", true);
            embed.addField(curRound.getOption2().getName(), curRound.getOption2().getValue() + " Searches", true);

            event.editMessage(new MessageEditBuilder()
                    .setEmbeds(embed.build())
                    .setActionRow(
                            Button.primary("higherlower:next", "Next Round"),
                            Button.danger("higherlower:stop", "Stop Game")
                    ).build()).complete();
        } else if (split[1].equals("next")) {
            if (!isFinished()) {
                HigherLowerRound nextRound = rounds.get(this.round);
                event.editMessage(createMessage(nextRound.getOption1(), nextRound.getOption2()).build()).complete();
            } else {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Game Over")
                        .setColor(Main.accentColor)
                        .setDescription("You got " + this.correct + " out of " + this.rounds.size() + " correct!");

                event.getInteraction().editMessage(new MessageEditBuilder()
                        .setEmbeds(embed.build())
                        .build()).complete();

                event.getInteraction().getMessage().editMessageComponents().complete();

                HigherLowerUtils.endGame(event.getUser().getId());
            }
        } else if (split[1].equals("stop")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Game Over")
                    .setColor(Main.accentColor)
                    .setDescription("You stopped the game!");

            event.getInteraction().editMessage(new MessageEditBuilder()
                    .setEmbeds(embed.build())
                    .build()).complete();

            event.getInteraction().getMessage().editMessageComponents().complete();

            HigherLowerUtils.endGame(event.getUser().getId());
        }
    }

    public void start() {
        HigherLowerRound curRound = rounds.get(this.round);
        hook.editOriginal(createMessage(curRound.getOption1(), curRound.getOption2()).build()).complete();
    }

    private MessageEditBuilder createMessage(HigherLowerOption option1, HigherLowerOption option2) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Higher or Lower")
                .setColor(Main.accentColor)
                .setDescription("Which one has more results on google?")
                .addField("Option 1", option1.getName(), true)
                .addField("Option 2", option2.getName(), true)
                .setFooter("Round " + (round + 1) + "/" + rounds.size());

        return new MessageEditBuilder()
                .setContent("")
                .setEmbeds(embed.build())
                .setActionRow(
                        Button.primary("higherlower:answer:1", option1.getName()),
                        Button.primary("higherlower:answer:2", option2.getName()),
                        Button.danger("higherlower:stop", "Stop Game")
                );
    }

    public boolean isFinished() {
        if (rounds.size() - 1 == round) {
            return true;
        } else {
            round++;
            return false;
        }
    }

    public InteractionHook getHook() {
        return hook;
    }
}
