package flustix.fluxifyed;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.APIServer;
import flustix.fluxifyed.listeners.*;
import flustix.fluxifyed.utils.module.ModuleUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
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
    public static final int accentColor = 0xef6624;

    private static JDA bot;
    private static JsonObject config;
    private static final long startTime = System.currentTimeMillis();
    private static final List<Module> modules = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        LOGGER.info("Starting Fluxifyed...");

        config = JsonParser.parseString(Files.readString(Path.of("config.json"))).getAsJsonObject();

        Database.initializeDataSource();

        Thread apiThread = new Thread(() -> {
            try {
                APIServer.main();
            } catch (Exception e) {
                LOGGER.error("Error while starting API Server", e);
            }
        });
        apiThread.setName("API Server");
        apiThread.start();

        EnumSet<GatewayIntent> intents = EnumSet.allOf(GatewayIntent.class);
        intents.remove(GatewayIntent.MESSAGE_CONTENT);

        JDABuilder builder = JDABuilder.create(config.get("token").getAsString(), intents);
        builder.setActivity(Activity.listening("music"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        initModules(builder);

        builder.addEventListeners(
                new ReadyListener(),
                new SlashCommandListener(),
                new GuildListener()
        );
        bot = builder.build();
    }

    static void initModules(JDABuilder builder) {
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
}
