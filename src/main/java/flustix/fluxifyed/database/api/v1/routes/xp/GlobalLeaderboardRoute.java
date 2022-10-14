package flustix.fluxifyed.database.api.v1.routes.xp;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.components.xp.GlobalLeaderboard;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.util.HashMap;

@APIRoute(path = "/xp/leaderboard/global")
public class GlobalLeaderboardRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        return new APIResponse(200, "OK", new GlobalLeaderboard());
    }
}
