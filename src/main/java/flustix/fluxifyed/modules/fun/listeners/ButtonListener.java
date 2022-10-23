package flustix.fluxifyed.modules.fun.listeners;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.fun.commands.RedditSlashCommand;
import flustix.fluxifyed.modules.fun.utils.higherlower.HigherLowerUtils;
import flustix.fluxifyed.modules.fun.utils.higherlower.components.HigherLowerGame;
import flustix.fluxifyed.modules.fun.utils.reddit.RedditUtils;
import flustix.fluxifyed.modules.fun.utils.reddit.components.RedditMessage;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("reddit:next")) {
            if (!RedditSlashCommand.messages.containsKey(event.getMessageId())) return;

            String subreddit = RedditSlashCommand.messages.get(event.getMessageId());
            RedditMessage message = RedditUtils.getRedditPost(subreddit, event.getChannel().asTextChannel().isNSFW());
            event.editMessage(message.message).complete();
        }

        if (event.getComponentId().startsWith("higherlower")) {
            HigherLowerGame game = HigherLowerUtils.getGame(event.getUser().getId());
            if (game == null) return;

            game.answer(event);
        }
    }
}
