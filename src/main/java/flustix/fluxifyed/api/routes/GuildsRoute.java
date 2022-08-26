package flustix.fluxifyed.api.routes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.api.types.Route;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class GuildsRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        JsonObject json = new JsonObject();

        JsonArray guilds = new JsonArray();

        for (JDA shard : Main.getShards()) {
            for (Guild guild : shard.getGuilds()) {
                JsonObject guildJson = new JsonObject();
                guildJson.addProperty("id", guild.getId());
                guildJson.addProperty("name", guild.getName());
                guildJson.addProperty("owner", guild.getOwnerId());
                guildJson.addProperty("shard", shard.getShardInfo().getShardId());
                guilds.add(guildJson);
            }
        }

        json.add("guilds", guilds);
        return json;
    }
}
