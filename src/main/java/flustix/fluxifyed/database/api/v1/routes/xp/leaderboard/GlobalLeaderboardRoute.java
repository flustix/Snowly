package flustix.fluxifyed.database.api.v1.routes.xp.leaderboard;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.components.xp.GlobalLeaderboard;
import flustix.fluxifyed.database.api.v1.types.Route;
import flustix.fluxifyed.utils.json.JSONUtils;

import java.util.HashMap;

public class GlobalLeaderboardRoute implements Route {
    public JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception {
        return JSONUtils.toJson(new GlobalLeaderboard()).getAsJsonObject();
    }
}
