using DSharpPlus.Entities;
using Newtonsoft.Json;

namespace Snowly.Components.Message;

public class CustomMessage
{
    [JsonProperty("content")]
    public string Content { get; set; }

    [JsonProperty("embed")]
    public CustomEmbed Embed { get; set; }

    public DiscordEmbed ToEmbed() => Embed?.Build();
}
