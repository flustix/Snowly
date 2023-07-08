using System.Numerics;
using DSharpPlus;
using DSharpPlus.Entities;
using DSharpPlus.EventArgs;
using DSharpPlus.Exceptions;
using Fluxifyed.CC;
using Fluxifyed.Commands;
using Fluxifyed.Config;
using Fluxifyed.Image;
using Fluxifyed.Image.Drawables;
using Fluxifyed.Image.Drawables.Shapes;
using Fluxifyed.Listeners;
using Fluxifyed.Logging;
using Fluxifyed.Modules;
using Fluxifyed.Modules.Economy;
using Fluxifyed.Modules.Timers;
using Fluxifyed.Modules.Utility;
using Fluxifyed.Modules.Welcome;
using Fluxifyed.Modules.XP;
using Fluxifyed.Utils;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;

namespace Fluxifyed; 

public static class Fluxifyed {
    public static DiscordClient Bot { get; private set; }
    public static BotConfig Config { get; private set; }
    
    // flags
    private static string ConfigFile { get; set; }
    public static bool IsDebug { get; private set; }
    
    public static List<IModule> Modules { get; private set; }
    public static List<ISlashCommand> SlashCommands { get; private set; }
    
    private static LoggerFactory LoggerFactory { get; } = new();
    public static ILogger Logger { get; } = LoggerFactory.CreateLogger("Fluxifyed");
    
    private static readonly List<DiscordApplicationCommand> List = new();

    public static async Task Main(string[] args) {
        if (args.Contains("--image")) {
            ImageTest();
            return;
        }

        if (Bot != null) throw new Exception("Bot is already running!");
        
        ConsoleCommands.Init();
        Console.Title = "Fluxifyed";
        Console.Clear();
        
        ConfigFile = args.Contains("--config") ? args[args.ToList().IndexOf("--config") + 1] : "config.json";
        IsDebug = args.Contains("--debug");
        
        if (IsDebug) {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("Debug mode enabled!");
            Console.ResetColor();
        }

        await Run();
    }

    private static async Task Run() {
        if (!File.Exists(ConfigFile)) {
            Logger.LogError($"Config file '{ConfigFile}' does not exist!");
            return;
        }
        
        Config = JsonConvert.DeserializeObject<BotConfig>(await File.ReadAllTextAsync(ConfigFile));
        
        Bot = new DiscordClient(new DiscordConfiguration {
            Token = Config.Token,
            TokenType = TokenType.Bot,
            Intents = DiscordIntents.AllUnprivileged | DiscordIntents.GuildMembers | DiscordIntents.GuildPresences,
            AutoReconnect = true,
            MinimumLogLevel = IsDebug ? LogLevel.Debug : LogLevel.Information,
            LoggerFactory = LoggerFactory
        });

        Bot.Ready += Ready;
        Bot.InteractionCreated += SlashListener.OnSlashCommand;
        
        RegisterListeners();
        LoadModules();

        await Bot.ConnectAsync();
        await Task.Delay(-1);
    }

    private static void LoadModules() {
        Modules = new List<IModule>();
        SlashCommands = new List<ISlashCommand>();
        
        LoadModule(new EconomyModule(), List);
        LoadModule(new UtilityModule(), List);
        LoadModule(new XpModule(), List);
        LoadModule(new TimersModule(), List);
        LoadModule(new WelcomeModule(), List);
    }

    private static async Task Ready(DiscordClient sender, ReadyEventArgs args) {
        Logger.LogInformation($"Logged in as {Bot.CurrentUser.Username}#{Bot.CurrentUser.Discriminator}");

        try {
            Logger.LogInformation("Overwriting global slash commands...");
            await Bot.BulkOverwriteGlobalApplicationCommandsAsync(List.ToArray());
        } catch (BadRequestException e) {
            Logger.LogError(e, "Failed to load modules!");
            File.WriteAllText("error.log", e.WebResponse.Response);
            return;
        }

        
        Logger.LogInformation("Ready!");
    }

    private static void LoadModule(IModule module, ICollection<DiscordApplicationCommand> list) {
        Logger.LogDebug($"[Module] {module.Name}");
        
        foreach (var command in module.SlashCommands) {
            SlashCommands.Add(command);
            
            var cmd = CommandBuilder.BuildCommand(command);
            if (cmd is null) continue;
            list.Add(cmd);
        }
        
        Modules.Add(module);
    }

    private static void RegisterListeners() {
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

    private static void ImageTest() {
        var renderer = new ImageRenderer {
            Path = "test.png",
            Size = new Vector2(1200, 500)
        };
        
        renderer.AddRange(new Drawable[] {
            new Box {
                X = 300,
                Width = 100,
                Height = 100,
                CornerRadius = 20,
            }
        });
        
        renderer.Render();
    }
}