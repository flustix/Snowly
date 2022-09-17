package flustix.fluxifyed;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.api.APIServer;
import flustix.fluxifyed.command.SlashCommandList;
import flustix.fluxifyed.console.ConsoleCommands;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.listeners.GuildListener;
import flustix.fluxifyed.listeners.MessageListener;
import flustix.fluxifyed.listeners.ReadyListener;
import flustix.fluxifyed.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger("Fluxifyed");
    public static final int accentColor = 0xef6624;

    private static JDA bot;
    private static JsonObject config;
    private static final String version = "2022.3.0";

    public static void main(String[] args) throws Exception {
        config = JsonParser.parseString(Files.readString(Path.of("config.json"))).getAsJsonObject();

        Database.initializeDataSource();
        SlashCommandList.initializeList();

        Thread apiThread = new Thread(() -> {
            try {
                APIServer.main();
            } catch (Exception e) {
                LOGGER.error("Error while starting API Server", e);
            }
        });
        apiThread.setName("API Server");
        apiThread.start();

        Thread consoleThread = new Thread(() -> {
            try {
                ConsoleCommands.start();
            } catch (Exception ignored) {}
        });
        consoleThread.setName("Fluxifyed Console");
        consoleThread.start();

        EnumSet<GatewayIntent> intents = EnumSet.allOf(GatewayIntent.class);
        intents.remove(GatewayIntent.MESSAGE_CONTENT);

        JDABuilder builder = JDABuilder.createDefault(config.get("token").getAsString());
        builder.enableIntents(intents);
        builder.setActivity(Activity.listening("/help"));
        builder.addEventListeners(
                new MessageListener(),
                new ReadyListener(),
                new SlashCommandListener(),
                new GuildListener()
        );
        bot = builder.build();
    }

    public static JsonObject getConfig() {
        return config;
    }

    public static JDA getBot() {
        return bot;
    }

    public static String getVersion() {
        return version;
    }
}
