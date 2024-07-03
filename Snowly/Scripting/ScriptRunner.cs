using DSharpPlus.Entities;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using NLua;
using Snowly.Scripting.Models.Channels.Messages.Embed;
using Snowly.Scripting.Models.Channels.Messages.Polls;
using Snowly.Utils;

namespace Snowly.Scripting;

public class ScriptRunner
{
    private DiscordInteraction interaction { get; }
    private Lua lua { get; }
    private Dictionary<string, ILuaModel> context { get; } = new();
    private bool hasReplied { get; set; }

    public ScriptRunner(DiscordInteraction interaction)
    {
        this.interaction = interaction;

        lua = new Lua();
        lua.DoString("import = function() end"); // disable importing

        AddFunction("reply", replyContent);
        AddFunction("reply", replyEmbed);
        AddFunction("reply", replyPoll);

        AddFunction("createEmbed", () => new LuaEmbed());
        AddFunction("Poll", (string question) => new LuaPoll { Question = new LuaPollText { Text = question } });
    }

    public void AddFunction(string name, Delegate function)
    {
        lua.RegisterFunction(name, function.Target, function.Method);
    }

    public void AddContext(string name, ILuaModel value)
    {
        context[name] = value;
        lua["ctx"] = context;
    }

    public void Run(string code)
    {
        lua.DoString(code);

        if (!hasReplied)
            interaction.Reply("*<empty>*", true);
    }

    private void replyContent(string content)
    {
        if (string.IsNullOrWhiteSpace(content))
            return;

        interaction.Reply(content);
        hasReplied = true;
    }

    private void replyEmbed(LuaEmbed embed)
    {
        interaction.ReplyEmbed(embed.ToCustomEmbed());
        hasReplied = true;
    }

    private void replyPoll(LuaPoll poll)
    {
        try
        {
            Snowly.Logger.LogDebug(JsonConvert.SerializeObject(poll));
            var builder = new DiscordPollBuilder
            {
                Question = poll.Question.Text,
                Duration = poll.Duration,
                IsMultipleChoice = poll.MultiSelect
            };

            foreach (var answer in poll.Answers)
            {
                /*var emote = new DiscordComponentEmoji(answer.Emote);*/
                builder.AddOption(answer.Text /*, answer.Emote == 0 ? null : emote*/);
            }

            interaction.CreateResponseAsync(DiscordInteractionResponseType.ChannelMessageWithSource, new DiscordInteractionResponseBuilder().WithPoll(builder)).Wait();
        }
        catch (Exception ex)
        {
            Snowly.Logger.LogError(ex, "Error sending poll!");
            throw;
        }

        hasReplied = true;
    }
}
