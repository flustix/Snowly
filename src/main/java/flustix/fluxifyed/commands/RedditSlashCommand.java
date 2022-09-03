package flustix.fluxifyed.commands;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.command.SlashCommand;
import flustix.fluxifyed.utils.reddit.RedditUtils;
import flustix.fluxifyed.utils.reddit.types.RedditPost;
import flustix.fluxifyed.utils.slash.SlashCommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Date;

public class RedditSlashCommand extends SlashCommand {
    public RedditSlashCommand() {
        super("reddit", "Get a reddit post");
        addOption(OptionType.STRING, "subreddit", "The subreddit to get a post from", true, true);
    }

    public void execute(SlashCommandInteraction interaction) {
        String subreddit = interaction.getOption("subreddit").getAsString();

        SlashCommandUtils.reply(interaction, "Getting a post from r/" + subreddit + "...", (hook)->{
            RedditPost post = RedditUtils.getRandomPost(subreddit);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(post.getTitle())
                    .setDescription("[Original Post](https://reddit.com" + post.getUrl() + ")")
                    .setColor(Main.accentColor);

            Date created = new Date(post.getCreated());
            embed.setFooter("Posted " + created, "https://flustix.foxes4life.net/assets/twemoji/clock1.png"); // so i cant use discord's original svgs here?

            embed.addField(":bust_in_silhouette: Author", post.getAuthor(), true);
            embed.addField(":books: Subreddit", "r/" + post.getSubreddit(), true);
            embed.addField("<:upvote:711192739433152573> Upvotes", post.getUps() + "", true);

            if (post.isImage()) {
                embed.setImage(post.getImageUrl());
            }
            if (post.isText()) {
                String text = post.getText().replace("\n\n", "\n");
                if (text.length() > 1024) {
                    text = text.substring(0, 1021) + "...";
                }
                if (!text.isEmpty())
                    embed.addField(":pencil: Text", text, false);
            }

            if (post.isNsfw() && !interaction.getChannel().asTextChannel().isNSFW()) {
                embed = new EmbedBuilder()
                        .setTitle("This post is NSFW")
                        .setDescription("NSFW post can only be viewed in NSFW channels!")
                        .setColor(0xFF5555);
            } else {
                addAutocomplete("subreddit", post.getSubreddit());
            }

            hook.editOriginal(
                    new MessageBuilder()
                            .setEmbeds(
                                    embed.build()
                            ).build()
            ).complete();
        });
    }
}
