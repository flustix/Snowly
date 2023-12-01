using DSharpPlus.Entities;
using Newtonsoft.Json;
using Snowly.Constants;

namespace Snowly.Components.Message;

public class CustomEmbed {
    [JsonProperty("title")]
    public string Title { get; set; }

    [JsonProperty("description")]
    public string Description { get; set; }

    [JsonProperty("url")]
    public string Url { get; set; }

    [JsonProperty("color")]
    public string ColorHex { get; set; }

    [JsonIgnore]
    public DiscordColor Color {
        get => ColorHex == null ? DiscordColor.White : Colors.FromHex(ColorHex);
        init => ColorHex = value.ToString();
    }

    [JsonProperty("image")]
    public string ImageUrl { get; set; }

    [JsonProperty("thumbnail")]
    public string ThumbnailUrl { get; set; }

    [JsonProperty("footer")]
    public CustomEmbedFooter Footer { get; set; }

    [JsonProperty("author")]
    public CustomEmbedAuthor Author { get; set; }

    [JsonProperty("fields")]
    public List<CustomEmbedField> Fields { get; set; }

    public DiscordEmbed Build() {
        var embed = new DiscordEmbedBuilder{
            Title = Title,
            Description = Description,
            Url = Url,
            Color = Color,
            ImageUrl = ImageUrl,
            Thumbnail = new DiscordEmbedBuilder.EmbedThumbnail {
                Url = ThumbnailUrl
            },
            Footer = Footer?.Build(),
            Author = Author?.Build()
        };

        Fields?.ForEach(x => embed.AddField(x.Name, x.Value, x.Inline));

        return embed;
    }

    public void AddField(string title, string value, bool inline = false) {
        Fields ??= new List<CustomEmbedField>();

        Fields.Add(new CustomEmbedField {
            Name = title,
            Value = value,
            Inline = inline
        });
    }
}
