package flustix.fluxifyed.database.api.v1.types;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

public interface Route {
    APIResponse execute(HttpExchange exchange, HashMap<String, String> params) throws Exception;
}
