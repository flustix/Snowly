package flustix.fluxifyed.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import flustix.fluxifyed.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIServer {
    public static Logger LOGGER = LoggerFactory.getLogger("FluxifyedAPI");

    public static void main() throws Exception {
        Router router = new Router();

        router.addRoute("/guilds", (exchange) -> {
            JsonObject json = new JsonObject();

            JsonArray guilds = new JsonArray();

            for (JDA shard : Main.getShards()) {
                for (Guild guild : shard.getGuilds()) {
                    JsonObject guildJson = new JsonObject();
                    guildJson.addProperty("id", guild.getId());
                    guildJson.addProperty("name", guild.getName());
                    guildJson.addProperty("shard", shard.getShardInfo().getShardId());
                    guilds.add(guildJson);
                }
            }

            json.add("guilds", guilds);
            return json;
        });

        int port = 8080;

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", router);
        server.setExecutor(null);
        server.start();

        LOGGER.info("API Server started on port {}", port);
    }
}
