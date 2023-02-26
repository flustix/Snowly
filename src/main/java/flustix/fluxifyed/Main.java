package flustix.fluxifyed;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.APIServer;
import flustix.fluxifyed.listeners.GuildListener;
import flustix.fluxifyed.listeners.ReadyListener;
import flustix.fluxifyed.listeners.SlashCommandListener;
import flustix.fluxifyed.localization.Localization;
import flustix.fluxifyed.utils.ModuleUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Main {
    public static final Logger LOGGER = LoggerFactory.getLogger("Fluxifyed");

    private static JDA bot;
    private static JsonObject config;
    private static final long startTime = System.currentTimeMillis();
    private static final List<Module> modules = new ArrayList<>();
    private static List<Permission> requiredPermissions;

    public static void main(String[] args) throws Exception {
        Localization.init();

        LOGGER.info("Starting Fluxifyed...");

        config = JsonParser.parseString(Files.readString(Path.of("config.json"))).getAsJsonObject();

        Database.initializeDataSource();
        APIServer.main();

        EnumSet<GatewayIntent> intents = EnumSet.allOf(GatewayIntent.class);
        initReqPerms();

        JDABuilder builder = JDABuilder.create(config.get("token").getAsString(), intents);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.addEventListeners(new ReadyListener(), new SlashCommandListener(), new GuildListener());
        initModules(builder);
        bot = builder.build();
    }

    private static void initModules(JDABuilder builder) {
        Reflections reflections = new Reflections("flustix.fluxifyed.modules");
        reflections.getSubTypesOf(Module.class).forEach(m -> {
            try {
                Module module = m.getDeclaredConstructor().newInstance();
                module.init();

                ModuleUtils.loadCommands(module);
                builder.addEventListeners(ModuleUtils.loadListeners(module).toArray());

                modules.add(module);
                LOGGER.info("Loaded module " + module.name + " (" + module.id + ")" + "!");
            } catch (Exception e) {
                LOGGER.error("Error while initializing module " + m.getName(), e);
            }
        });
    }

    private static void initReqPerms() {
        requiredPermissions = List.of(
                Permission.MANAGE_ROLES,
                Permission.KICK_MEMBERS,
                Permission.BAN_MEMBERS,
                Permission.VIEW_CHANNEL,
                Permission.MODERATE_MEMBERS,
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_SEND_IN_THREADS,
                Permission.MESSAGE_MANAGE,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.MESSAGE_ADD_REACTION);
    }

    public static JsonObject getConfig() {
        return config;
    }

    public static JDA getBot() {
        return bot;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static List<Permission> getRequiredPermissions() {
        return requiredPermissions;
    }
}
