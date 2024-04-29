using Newtonsoft.Json;

namespace Snowly.Components.Message;

public class CustomEmbedField
{
    [JsonProperty("name")]
    public string Name { get; set; }

    [JsonProperty("value")]
    public string Value { get; set; }

    [JsonProperty("inline")]
    public bool Inline { get; set; }

    public CustomEmbedField(string name, string value, bool inline)
    {
        Name = name;
        Value = value;
        Inline = inline;
    }

    [JsonConstructor]
    public CustomEmbedField()
    {
    }
}
