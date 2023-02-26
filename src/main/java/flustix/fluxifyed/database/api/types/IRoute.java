package flustix.fluxifyed.database.api.types;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

public interface IRoute {
    APIResponse execute(HttpExchange exchange, HashMap<String, String> params);
}
