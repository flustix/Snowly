using DSharpPlus.Entities;
using Newtonsoft.Json;

namespace Fluxifyed.Components.Message; 

public class CustomEmbedAuthor {
    [JsonProperty("name")]
    public string Name { get; set; }
    
    [JsonProperty("icon")]
    public string IconUrl { get; set; }
    
    [JsonProperty("url")]
    public string Url { get; set; }
    
    public DiscordEmbedBuilder.EmbedAuthor Build() {
        return new DiscordEmbedBuilder.EmbedAuthor {
            Name = Name,
            IconUrl = IconUrl,
            Url = Url
        };
    }
}