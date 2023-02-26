package flustix.fluxifyed.database.api.routes;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.IRoute;

import java.util.HashMap;

@APIRoute(path = "/")
public class IndexRoute implements IRoute {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        return new APIResponse(200, "Welcome to the Fluxifyed API! If you don't know what to do, check out the documentation at https://fluxifyed.foxes4life.net/docs/api.", null);
    }
}
