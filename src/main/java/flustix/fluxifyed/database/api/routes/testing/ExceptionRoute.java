package flustix.fluxifyed.database.api.routes.testing;

import com.sun.net.httpserver.HttpExchange;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.IRoute;

import java.util.HashMap;

@APIRoute(path = "/testing/exception")
public class ExceptionRoute implements IRoute {
    public APIResponse execute(HttpExchange exchange, HashMap<String, String> params) {
        throw new RuntimeException("Forced test exception");
    }
}