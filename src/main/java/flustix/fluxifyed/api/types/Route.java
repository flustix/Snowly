package flustix.fluxifyed.api.types;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

public interface Route {
    JsonObject execute(HttpExchange exchange) throws Exception;
}
