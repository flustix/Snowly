package flustix.fluxifyed;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.api.APIServer;
import flustix.fluxifyed.command.CommandList;
import flustix.fluxifyed.command.SlashCommandList;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.listeners.MessageListener;
import flustix.fluxifyed.listeners.ReadyListener;
import flustix.fluxifyed.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger("Fluxifyed");
    public static final int accentColor = 0xef6624;

    private static JsonObject config;
    private static final String prefix = "flux ";
    private static final int maxShards = 1;

    public static void main(String[] args) throws Exception {
        config = JsonParser.parseString(Files.readString(Path.of("config.json"))).getAsJsonObject();

        Database.initializeDataSource();
        CommandList.initializeList();
        SlashCommandList.initializeList();

        APIServer.main();

        EnumSet<GatewayIntent> intents = EnumSet.allOf(GatewayIntent.class);
        intents.remove(GatewayIntent.MESSAGE_CONTENT);

        JDABuilder shardBuilder = JDABuilder.createDefault(config.get("token").getAsString());
        shardBuilder.enableIntents(intents);
        shardBuilder.setActivity(Activity.listening("/help"));
        shardBuilder.addEventListeners(
                new MessageListener(),
                new ReadyListener(),
                new SlashCommandListener()
        );

        try {
            for (int i = 0; i < maxShards; i++) {
                shardBuilder.useSharding(i, maxShards).build();
            }
        } catch(LoginException ignored) {
            LOGGER.error("Failed to log in!");
            System.exit(1);
        }
    }

    public static JsonObject getConfig() {
        return config;
    }

    public static String getPrefix() {
        return prefix;
    }
}
