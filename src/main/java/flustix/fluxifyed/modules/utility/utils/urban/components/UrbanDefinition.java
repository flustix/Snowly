package flustix.fluxifyed.modules.utility.utils.urban.components;

public class UrbanDefinition {
    final String word;
    final String definition;
    final String example;
    final String author;
    final String permalink;
    final int thumbsUp;
    final int thumbsDown;

    public UrbanDefinition(String word, String definition, String example, String author, String permalink, int thumbsUp, int thumbsDown) {
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.author = author;
        this.permalink = permalink;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public String getAuthor() {
        return author;
    }

    public String getPermalink() {
        return permalink;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }
}

