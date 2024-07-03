using NLua;

namespace Snowly.Scripting.Models.Channels.Messages.Polls;

public class LuaPoll : ILuaModel
{
    [LuaMember(Name = "question")]
    public LuaPollText Question { get; set; }

    [LuaMember(Name = "answers")]
    public List<LuaPollText> Answers { get; set; } = new();

    [LuaMember(Name = "duration")]
    public int Duration { get; set; }

    [LuaMember(Name = "multiselect")]
    public bool MultiSelect { get; set; }

    [LuaMember(Name = "add")]
    public void AddAnswer(string answer) => Answers.Add(new LuaPollText { Text = answer });
}
