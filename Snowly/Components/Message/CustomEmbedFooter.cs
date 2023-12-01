using DSharpPlus.Entities;
using Newtonsoft.Json;

namespace Snowly.Components.Message;

public class CustomEmbedFooter {
    [JsonProperty("text")]
    public string Text { get; set; }

    [JsonProperty("icon")]
    public string IconUrl { get; set; }

    public DiscordEmbedBuilder.EmbedFooter Build() {
        return new DiscordEmbedBuilder.EmbedFooter {
            Text = Text,
            IconUrl = IconUrl
        };
    }
}
