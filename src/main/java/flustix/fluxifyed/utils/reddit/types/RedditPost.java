package flustix.fluxifyed.utils.reddit.types;

public class RedditPost {
    String title;
    String author;
    String subreddit;
    String url;
    long created;
    boolean nsfw;
    int ups;
    boolean isText = false;
    String text;
    boolean isImage = false;
    String imageUrl;

    public RedditPost(String title, String author, String subreddit, String url, long created, boolean nsfw, int ups) {
        this.title = title;
        this.author = author;
        this.subreddit = subreddit;
        this.url = url;
        this.created = created;
        this.nsfw = nsfw;
        this.ups = ups;
    }

    public void setImage(String imageUrl) {
        isImage = true;
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        isText = true;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public long getCreated() {
        return created;
    }

    public int getUps() {
        return ups;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public boolean isText() {
        return isText;
    }

    public boolean isImage() {
        return isImage;
    }
}
