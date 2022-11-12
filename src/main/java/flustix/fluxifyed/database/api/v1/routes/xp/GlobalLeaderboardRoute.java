package flustix.fluxifyed.database.api.v1.routes.xp;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.utils.QueryUtils;
import flustix.fluxifyed.database.api.v1.components.xp.GlobalLeaderboard;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.util.HashMap;

@APIRoute(path = "/modules/xp/global/")
public class GlobalLeaderboardRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        HashMap<String, String> query = QueryUtils.getQuery(exchange.getRequestURI().getQuery());

        int limit = query.containsKey("limit") ? Integer.parseInt(query.get("limit")) : 20;
        int offset = query.containsKey("offset") ? Integer.parseInt(query.get("offset")) : 0;

        return new APIResponse(200, "OK", new GlobalLeaderboard(limit, offset));
    }
}
