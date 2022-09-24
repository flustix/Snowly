package flustix.fluxifyed.database.api.types;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

public interface Route {
    JsonObject execute(HttpExchange exchange, HashMap<String, String> params) throws Exception;
}
