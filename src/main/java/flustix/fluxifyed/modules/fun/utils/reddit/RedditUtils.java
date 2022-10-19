package flustix.fluxifyed.modules.fun.utils.reddit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.modules.fun.utils.reddit.components.RedditMessage;
import flustix.fluxifyed.modules.fun.utils.reddit.components.RedditPost;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

public class RedditUtils {
    public static RedditMessage getRedditPost(String subreddit, boolean channelNSFW) {
        RedditPost post = RedditUtils.getRandomPost(subreddit);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(post.getTitle())
                .setDescription("[Original Post](https://reddit.com" + post.getUrl() + ")")
                .setColor(Main.accentColor);

        if (post.isNsfw() && channelNSFW) {
            embed.addField(":underage: NSFW", "This post is NSFW", false);
        }

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

        if (post.isNsfw() && !channelNSFW) {
            embed = new EmbedBuilder()
                    .setTitle("This post is NSFW")
                    .setDescription("NSFW post can only be viewed in NSFW channels!")
                    .setColor(0xFF5555);
        }

        return new RedditMessage(new MessageEditBuilder()
                .setContent("")
                .setEmbeds(embed.build())
                .setActionRow(Button.primary("reddit:next", "Next"))
                .build(), post.getSubreddit(), post.isNsfw());
    }

    public static RedditPost getRandomPost(String subreddit) {
        String url = "https://www.reddit.com/r/" + subreddit + "/random.json";

        try {
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json, text/plain, /")
                    .header("User-Agent", "Fluxifyed/latest (by /u/Flustix)")
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            return parsePost(res.body());
        } catch (Exception ex) {
            Main.LOGGER.error("Error getting random post from r/" + subreddit, ex);
            return new RedditPost("Not Found", "NotFound", "NotFound", "", System.currentTimeMillis(), false, 0);
        }
    }

    public static RedditPost parsePost(String response) {
        JsonObject apiData = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();
        JsonObject data = apiData.getAsJsonObject("data")
                .getAsJsonArray("children")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("data");

        RedditPost post = new RedditPost(
                data.get("title").getAsString(),
                data.get("author").getAsString(),
                data.get("subreddit").getAsString(),
                data.get("permalink").getAsString(),
                data.get("created").getAsLong() * 1000L,
                data.get("over_18").getAsBoolean(),
                data.get("ups").getAsInt()
        );

        String imageUrl = "";

        if (data.get("post_hint") != null) {
            if (data.get("post_hint").getAsString().equals("image")) {
                imageUrl = data.get("url").getAsString();
            }
        }

        if (data.get("gallery_data") != null) {
            JsonObject galleryData = data.getAsJsonObject("gallery_data");
            JsonObject imageMetadata = data.getAsJsonObject("media_metadata").getAsJsonObject(galleryData.getAsJsonArray("items").get(0).getAsJsonObject().get("media_id").getAsString());

            if (imageMetadata.get("e").getAsString().equals("Image")) {
                imageUrl = imageMetadata.getAsJsonObject("s").get("u").getAsString().replace("&amp;", "&");
            } else if (imageMetadata.get("e").getAsString().equals("AnimatedImage")) {
                imageUrl = imageMetadata.getAsJsonObject("s").get("gif").getAsString().replace("&amp;", "&");
            }
        }

        if (!imageUrl.isEmpty()) {
            post.setImage(imageUrl);
        }

        if (data.get("selftext") != null) {
            post.setText(data.get("selftext").getAsString());
        }

        return post;
    }
}
