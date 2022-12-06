package flustix.fluxifyed.modules.fun.commands;

import flustix.fluxifyed.components.Autocomplete;
import flustix.fluxifyed.components.SlashCommand;
import flustix.fluxifyed.modules.fun.utils.reddit.RedditUtils;
import flustix.fluxifyed.modules.fun.utils.reddit.components.RedditInteraction;
import flustix.fluxifyed.modules.fun.utils.reddit.components.RedditMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.HashMap;
import java.util.Map;

public class RedditSlashCommand extends SlashCommand {
    public static final Map<String, RedditInteraction> messages = new HashMap<>(); // <messageId, interaction>

    public RedditSlashCommand() {
        super("reddit", "Get a reddit post");
        addOption(OptionType.STRING, "subreddit", "The subreddit to get a post from", true, true);
    }

    public void execute(SlashCommandInteraction interaction) {
        OptionMapping subredditMapping = interaction.getOption("subreddit");
        if (subredditMapping == null) return;
        String subreddit = subredditMapping.getAsString();

        if (!RedditUtils.validateSubreddit(subreddit)) {
            EmbedBuilder invalidSubredditEmbed = new EmbedBuilder()
                    .setTitle("The subreddit you requested contains invalid characters")
                    .setDescription("Valid characters are: a-z, A-Z, 0-9 and underscores")
                    .addField("Requested subreddit", subreddit, false)
                    .setColor(0xFF5555);
            interaction.replyEmbeds(invalidSubredditEmbed.build()).setEphemeral(true).queue();

            return;
        }

        interaction.reply("Getting a post from r/" + subreddit + "...").queue((hook) -> {
            RedditMessage message = RedditUtils.getRedditPost(subreddit, interaction.getChannel().asTextChannel().isNSFW());
            if (!message.nsfw) addAutocomplete("subreddit", new Autocomplete("r/" + message.subreddit, message.subreddit));
            hook.editOriginal(message.message).queue(
                    (msg) -> messages.put(msg.getId(), new RedditInteraction(message.subreddit, interaction.getUser().getId()))
            );
        });
    }
}
