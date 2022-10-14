package flustix.fluxifyed.utils.reddit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.utils.reddit.types.RedditPost;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RedditUtils {
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
