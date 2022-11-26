package flustix.fluxifyed.database.api.v1.routes.testing;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.util.HashMap;

@APIRoute(path = "/testing/exception")
public class ExceptionTestRoute implements Route {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        throw new RuntimeException("Forced test exception");
    }
}