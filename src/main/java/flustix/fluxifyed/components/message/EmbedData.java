package flustix.fluxifyed.components.message;

public class EmbedData {
    public String title;
    public String description;
    public String url;
    public String color;
    public String thumbnail;
    public String image;
    public FieldData[] fields;
    public AuthorData author;
    public FooterData footer;

    public String toString() {
        return "EmbedData(title=" + this.title + ", description=" + this.description + ", url=" + this.url + ", color=" + this.color + ", thumbnail=" + this.thumbnail + ", image=" + this.image + ", fields=" + java.util.Arrays.deepToString(this.fields) + ", author=" + this.author + ", footer=" + this.footer + ")";
    }
}
