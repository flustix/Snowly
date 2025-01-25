using System.Numerics;
using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using DSharpPlus.Exceptions;
using Midori.Logging;
using SixLabors.ImageSharp.PixelFormats;
using Snowly.Commands;
using Snowly.Config;
using Snowly.Database;
using Snowly.Listeners;
using Snowly.Modules;
using Snowly.Modules.AutoResponder;
using Snowly.Modules.Economy;
using Snowly.Modules.Fun;
using Snowly.Modules.Timers;
using Snowly.Modules.Utility;
using Snowly.Modules.Welcome;
using Snowly.Modules.XP;
using Snowly.Renderer;
using Snowly.Renderer.Drawables;
using Snowly.Renderer.Drawables.Shapes;
using Snowly.Renderer.Drawables.Sprites;
using Snowly.Utils;

namespace Snowly;

public static class Snowly
{
    public static DiscordClient Bot { get; private set; }
    private static BotConfig config { get; set; }

    public static List<IModule> Modules { get; private set; }
    public static List<ISlashCommand> SlashCommands { get; private set; }

    private static readonly List<DiscordApplicationCommand> list = new();

    public static async Task Main(string[] args)
    {
        AppDomain.CurrentDomain.UnhandledException += (_, eventArgs) =>
        {
            if (eventArgs.ExceptionObject is not Exception e)
                Logger.Log($"Unhandled exception: {eventArgs.ExceptionObject}", LoggingTarget.General, LogLevel.Error);
            else
                Logger.Error(e, $"Unhandled exception: {e.Message}");
        };

        if (args.Contains("--image"))
        {
            imageTest();
            return;
        }

        if (Bot != null)
            throw new Exception("Bot is already running!");

        await run();
    }

    private static async Task run()
    {
        loadConfig();
        MongoDatabase.Setup(config.MongoConnection, config.Database);

        FontStorage.DefaultFont = "Renogare Soft";
        FontStorage.RegisterFont("Renogare Soft", "Resources/Fonts/RenogareSoft.ttf");

        Bot = new DiscordClient(new DiscordConfiguration
        {
            Token = config.Token,
            TokenType = TokenType.Bot,
            Intents = DiscordIntents.AllUnprivileged | DiscordIntents.GuildMembers | DiscordIntents.GuildPresences | DiscordIntents.MessageContents,
            AutoReconnect = true,
            MinimumLogLevel = Microsoft.Extensions.Logging.LogLevel.None
        });

        Bot.SessionCreated += ready;
        Bot.InteractionCreated += SlashListener.OnInteraction;

        registerListeners();
        loadModules();

        await Bot.ConnectAsync();
        await Task.Delay(-1);
    }

    private static void loadConfig()
    {
        var env = Environment.GetEnvironmentVariables();

        config = new BotConfig
        {
            Token = env["TOKEN"]?.ToString() ?? throw new Exception("TOKEN environment variable not set!"),
            MongoConnection = env["MONGO_CONNECTION"]?.ToString() ?? "mongodb://localhost:27017",
            Database = env["DATABASE"]?.ToString() ?? "snowly"
        };
    }

    private static void loadModules()
    {
        Modules = new List<IModule>();
        SlashCommands = new List<ISlashCommand>();

        loadModule(new EconomyModule(), list);
        loadModule(new UtilityModule(), list);
        loadModule(new XpModule(), list);
        loadModule(new TimersModule(), list);
        loadModule(new WelcomeModule(), list);
        loadModule(new FunModule(), list);
        loadModule(new AutoResponderModule(), list);
    }

    private static async Task ready(DiscordClient _, SessionReadyEventArgs __)
    {
        Logger.Log($"Logged in as {Bot.CurrentUser.Username}#{Bot.CurrentUser.Discriminator}");

        try
        {
            Logger.Log("Overwriting global slash commands...");
            await Bot.BulkOverwriteGlobalApplicationCommandsAsync(list.ToArray());
        }
        catch (BadRequestException e)
        {
            Logger.Error(e, "Failed to load modules!");
            await File.WriteAllTextAsync("error.log", e.Message);
            return;
        }

        var activityThread = new Thread(() =>
        {
            while (Bot != null)
            {
                var guilds = Bot.Guilds.Count;
                var members = Bot.Guilds.Values.Sum(guild => guild.MemberCount);

                const DiscordActivityType type = DiscordActivityType.Watching;
                var message = $"{guilds} guilds with {members} members";
                Bot.UpdateStatusAsync(new DiscordActivity(message, type));
                Logger.Log($"Set status to '{type} {message}'", level: LogLevel.Debug);
                Thread.Sleep(1000 * 60 * 5); // 5 minutes
            }
        });
        activityThread.Start();

        Logger.Log("Ready!");
    }

    private static void loadModule(IModule module, ICollection<DiscordApplicationCommand> list)
    {
        Logger.Log($"[Module] {module.Name}", level: LogLevel.Debug);

        foreach (var command in module.SlashCommands)
        {
            SlashCommands.Add(command);

            var cmd = CommandBuilder.BuildCommand(command);

            if (cmd is null)
                continue;

            list.Add(cmd);
        }

        Modules.Add(module);
    }

    private static void registerListeners()
    {
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

    private static void imageTest()
    {
        var renderer = new ImageRenderer
        {
            Path = "test.png",
            Size = new Vector2(1200, 500)
        };

        renderer.AddRange(new Drawable[]
        {
            new Box
            {
                X = 0,
                Width = 100,
                Height = 100
            },
            new Container
            {
                X = 50,
                Y = 50,
                Width = 80,
                Height = 80,
                Alpha = 0.5f,
                CornerRadius = 20,
                Drawables = new Drawable[]
                {
                    new Box
                    {
                        Width = 80,
                        Height = 80,
                        Color = new Rgba32(255, 0, 0)
                    },
                    new Box
                    {
                        X = 10,
                        Y = 10,
                        Width = 80,
                        Height = 80,
                        Color = new Rgba32(0, 255, 0)
                    }
                }
            },
            new Container
            {
                X = 200,
                Y = 200,
                Width = 80,
                Height = 80,
                CornerRadius = 20,
                Drawables = new Drawable[]
                {
                    new Sprite
                    {
                        Width = 80,
                        Height = 80,
                        Path = "https://cdn.discordapp.com/avatars/386436194709274627/b78ec7a8381886fa4be61202779c1d00.png?size=1024"
                    }
                }
            }
        });

        renderer.Render();
    }
}
