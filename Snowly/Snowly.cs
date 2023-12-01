using System.Numerics;
using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using DSharpPlus.Exceptions;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Database;
using Snowly.Image;
using Snowly.Image.Drawables;
using Snowly.Image.Drawables.Shapes;
using Snowly.Listeners;
using Snowly.Logging;
using Snowly.Modules;
using Snowly.Modules.Economy;
using Snowly.Modules.Fun;
using Snowly.Modules.Timers;
using Snowly.Modules.Utility;
using Snowly.Modules.Welcome;
using Snowly.Modules.XP;
using Snowly.Utils;

namespace Snowly;

public static class Snowly {
    public static DiscordClient Bot { get; private set; }
    private static BotConfig config { get; set; }

    // flags
    private static string configFile { get; set; }
    public static bool IsDebug { get; private set; }

    public static List<IModule> Modules { get; private set; }
    public static List<ISlashCommand> SlashCommands { get; private set; }

    private static LoggerFactory loggerFactory { get; } = new();
    public static ILogger Logger { get; } = loggerFactory.CreateLogger("Snowly");

    private static readonly List<DiscordApplicationCommand> list = new();

    public static async Task Main(string[] args) {
        await File.WriteAllTextAsync("snowly.log", string.Empty);

        AppDomain.CurrentDomain.UnhandledException += (_, eventArgs) => {
            if (eventArgs.ExceptionObject is not Exception e)
                Logger.LogError($"Unhandled exception: {eventArgs.ExceptionObject}");
            else
                Logger.LogError(e, $"Unhandled exception: {e.Message}");
        };

        if (args.Contains("--image")) {
            imageTest();
            return;
        }

        if (Bot != null) throw new Exception("Bot is already running!");

        Console.Title = "Snowly";
        Console.Clear();

        configFile = args.Contains("--config") ? args[args.ToList().IndexOf("--config") + 1] : "config.json";
        IsDebug = args.Contains("--debug");

        if (IsDebug) {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("Debug mode enabled!");
            Console.ResetColor();
        }

        await run();
    }

    private static async Task run() {
        if (!File.Exists(configFile)) {
            Logger.LogError($"Config file '{configFile}' does not exist!");
            return;
        }

        config = JsonConvert.DeserializeObject<BotConfig>(await File.ReadAllTextAsync(configFile));
        MongoDatabase.Setup(config.Database);

        FontStorage.DefaultFont = config.DefaultFont;
        foreach (var (name, path) in config.Fonts) FontStorage.RegisterFont(name, path);

        Bot = new DiscordClient(new DiscordConfiguration {
            Token = config.Token,
            TokenType = TokenType.Bot,
            Intents = DiscordIntents.AllUnprivileged | DiscordIntents.GuildMembers | DiscordIntents.GuildPresences,
            AutoReconnect = true,
            MinimumLogLevel = IsDebug ? LogLevel.Debug : LogLevel.Information,
            LoggerFactory = loggerFactory
        });

        Bot.Ready += ready;
        Bot.InteractionCreated += SlashListener.OnSlashCommand;

        registerListeners();
        loadModules();

        await Bot.ConnectAsync();
        await Task.Delay(-1);
    }

    private static void loadModules() {
        Modules = new List<IModule>();
        SlashCommands = new List<ISlashCommand>();

        loadModule(new EconomyModule(), list);
        loadModule(new UtilityModule(), list);
        loadModule(new XpModule(), list);
        loadModule(new TimersModule(), list);
        loadModule(new WelcomeModule(), list);
        loadModule(new FunModule(), list);
    }

    private static async Task ready(DiscordClient sender, ReadyEventArgs args) {
        Logger.LogInformation($"Logged in as {Bot.CurrentUser.Username}#{Bot.CurrentUser.Discriminator}");

        try {
            Logger.LogInformation("Overwriting global slash commands...");
            await Bot.BulkOverwriteGlobalApplicationCommandsAsync(list.ToArray());
        }
        catch (BadRequestException e) {
            Logger.LogError(e, "Failed to load modules!");
            await File.WriteAllTextAsync("error.log", e.WebResponse.Response);
            return;
        }

        var activityThread = new Thread(() =>
        {
            while (Bot != null)
            {
                var guilds = Bot.Guilds.Count;
                var members = Bot.Guilds.Values.Sum(guild => guild.MemberCount);

                const ActivityType type = ActivityType.Watching;
                var message = $"{guilds} guilds with {members} members";
                Bot.UpdateStatusAsync(new DiscordActivity(message, type));
                Logger.LogInformation($"Set status to '{type} {message}'");
                Thread.Sleep(1000 * 60 * 5); // 5 minutes
            }
        });
        activityThread.Start();

        Logger.LogInformation("Ready!");
    }

    private static void loadModule(IModule module, ICollection<DiscordApplicationCommand> list) {
        Logger.LogDebug($"[Module] {module.Name}");

        foreach (var command in module.SlashCommands) {
            SlashCommands.Add(command);

            var cmd = CommandBuilder.BuildCommand(command);
            if (cmd is null) continue;
            list.Add(cmd);
        }

        Modules.Add(module);
    }

    private static void registerListeners() {
        Bot.ChannelCreated += MultiListener.ChannelCreated;
        Bot.ChannelDeleted += MultiListener.ChannelDeleted;
        Bot.ChannelUpdated += MultiListener.ChannelUpdated;

        Bot.MessageCreated += MultiListener.OnMessageReceived;
        Bot.MessageDeleted += MultiListener.OnMessageDeleted;
        Bot.MessagesBulkDeleted += MultiListener.OnMessageBulkDeleted;
        Bot.MessageUpdated += MultiListener.OnMessageUpdated;

        Bot.MessageReactionAdded += MultiListener.OnReactionAdded;
        Bot.MessageReactionRemoved += MultiListener.OnReactionRemoved;
        Bot.MessageReactionsCleared += MultiListener.OnReactionsCleared;

        Bot.GuildRoleCreated += MultiListener.RoleCreated;
        Bot.GuildRoleDeleted += MultiListener.RoleDeleted;
        Bot.GuildRoleUpdated += MultiListener.RoleUpdated;

        Bot.GuildCreated += MultiListener.JoinedGuild;
        Bot.GuildDeleted += MultiListener.LeftGuild;
        Bot.GuildAvailable += MultiListener.GuildAvailable;
        Bot.GuildUnavailable += MultiListener.GuildUnavailable;
        Bot.GuildUpdated += MultiListener.GuildUpdated;

        Bot.IntegrationCreated += MultiListener.IntegrationCreated;
        Bot.IntegrationDeleted += MultiListener.IntegrationDeleted;
        Bot.IntegrationUpdated += MultiListener.IntegrationUpdated;

        Bot.GuildMemberAdded += MultiListener.UserJoined;
        Bot.GuildMemberRemoved += MultiListener.UserLeft;
        Bot.GuildBanAdded += MultiListener.UserBanned;
        Bot.GuildBanRemoved += MultiListener.UserUnbanned;
        Bot.GuildMemberUpdated += MultiListener.GuildMemberUpdated;

        Bot.TypingStarted += MultiListener.UserIsTyping;
        Bot.UserUpdated += MultiListener.UserUpdated;

        Bot.InviteCreated += MultiListener.InviteCreated;
        Bot.InviteDeleted += MultiListener.InviteDeleted;

        Bot.ComponentInteractionCreated += MultiListener.ComponentInteraction;
        Bot.ModalSubmitted += MultiListener.ModalSubmitted;

        Bot.ThreadCreated += MultiListener.ThreadCreated;
        Bot.ThreadDeleted += MultiListener.ThreadDeleted;
        Bot.ThreadUpdated += MultiListener.ThreadUpdated;
        Bot.ThreadMemberUpdated += MultiListener.ThreadMemberUpdated;

        Bot.StageInstanceCreated += MultiListener.StageStarted;
        Bot.StageInstanceDeleted += MultiListener.StageEnded;
        Bot.StageInstanceUpdated += MultiListener.StageUpdated;
    }

    private static void imageTest() {
        var renderer = new ImageRenderer {
            Path = "test.png",
            Size = new Vector2(1200, 500)
        };

        renderer.AddRange(new Drawable[] {
            new Box {
                X = 300,
                Width = 100,
                Height = 100,
                CornerRadius = 20
            }
        });

        renderer.Render();
    }
}
