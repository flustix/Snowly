package flustix.fluxifyed.modules.fun.utils.higherlower.components;

import flustix.fluxifyed.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class HigherLowerRound {
    private final HigherLowerOption option1;
    private final HigherLowerOption option2;
    private final int correctOption;

    public HigherLowerRound(HigherLowerOption option1, HigherLowerOption option2, int correctOption) {
        this.option1 = option1;
        this.option2 = option2;
        this.correctOption = correctOption;
    }

    public HigherLowerOption getOption1() {
        return option1;
    }

    public HigherLowerOption getOption2() {
        return option2;
    }

    public int getCorrectOption() {
        return correctOption;
    }
}
