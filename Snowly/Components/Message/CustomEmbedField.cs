using Newtonsoft.Json;

namespace Snowly.Components.Message; 

public class CustomEmbedField {
    [JsonProperty("name")]
    public string Name { get; set; }
    
    [JsonProperty("value")]
    public string Value { get; set; }
    
    [JsonProperty("inline")]
    public bool Inline { get; set; }
}