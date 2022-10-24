package flustix.fluxifyed.modules.fun.utils.reddit.components;

import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class RedditMessage {
    public final MessageEditData message;
    public final String subreddit;
    public final boolean nsfw;

    public RedditMessage(MessageEditData message, String subreddit, boolean nsfw) {
        this.message = message;
        this.subreddit = subreddit;
        this.nsfw = nsfw;
    }
}
