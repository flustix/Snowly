package flustix.fluxifyed.database.api.v1.routes;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.util.HashMap;

@APIRoute(path = "/")
public class IndexRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        return new APIResponse(200, "Welcome to the Fluxifyed API! If you don't know what to do, check out the documentation at https://fluxifyed.foxes4life.net/docs/api.", null);
    }
}
