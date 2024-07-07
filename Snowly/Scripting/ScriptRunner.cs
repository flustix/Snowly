using DSharpPlus.Entities;
using Microsoft.Extensions.Logging;
using NLua;
using Snowly.Scripting.Models.Channels.Messages.Embed;
using Snowly.Scripting.Models.Channels.Messages.Expressions;
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

        AddFunction("Embed", () => new LuaEmbed());
        AddFunction("Sticker", (ulong id) => new LuaSticker { ID = id });
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
            var message = new DiscordInteractionResponseBuilder().WithPoll(poll.Build());
            interaction.CreateResponseAsync(DiscordInteractionResponseType.ChannelMessageWithSource, message).Wait();
        }
        catch (Exception ex)
        {
            Snowly.Logger.LogError(ex, "Error sending poll!");
            throw;
        }

        hasReplied = true;
    }
}
