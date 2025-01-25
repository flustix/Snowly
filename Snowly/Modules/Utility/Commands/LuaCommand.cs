using DSharpPlus.Entities;
using Midori.Logging;
using Snowly.Commands;
using Snowly.Components.Message;
using Snowly.Scripting;
using Snowly.Scripting.Extensions;
using Snowly.Utils;

namespace Snowly.Modules.Utility.Commands;

public class LuaCommand : IOptionSlashCommand
{
    public string Name => "lua";
    public string Description => "Run snippets of Lua code.";
    public bool AllowInDM => true;

    public List<SlashOption> Options => new()
    {
        new SlashOption
        {
            Name = "code",
            Description = "The Lua code to run",
            Type = DiscordApplicationCommandOptionType.String,
            Required = true
        }
    };

    public void Handle(DiscordInteraction interaction)
    {
        if (interaction.User.Id != 386436194709274627)
        {
            interaction.Reply("You do not have permission to run this command.", true);
            return;
        }

        var code = interaction.GetString("code");
        var runner = new ScriptRunner(interaction);

        // ReSharper disable once ConditionIsAlwaysTrueOrFalse
        // this library sucks sometimes...
        if (interaction.Guild != null)
            runner.AddContext("guild", interaction.Guild.ToLua());

        runner.AddContext("channel", interaction.Channel.ToLua());

        try
        {
            runner.Run(code);
        }
        catch (Exception e)
        {
            interaction.ReplyEmbed(new CustomEmbed
            {
                Title = "An error occurred while running Lua code",
                Description = $"```{e.Message}```",
                Color = DiscordColor.Red
            }, true);

            Logger.Error(e, "An error occurred while running Lua code");
        }
    }
}
