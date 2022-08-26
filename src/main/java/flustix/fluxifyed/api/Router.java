package flustix.fluxifyed.api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import flustix.fluxifyed.api.types.Route;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;

public class Router implements HttpHandler {
    private static HashMap<String, Route> routes = new HashMap<>();

    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        int responseCode = 200;

        JsonObject json = new JsonObject();
        json.addProperty("code", 0); // do this first so it is always at the top

        if (routes.containsKey(exchange.getRequestURI().getPath())) {
            try {
                Route route = routes.get(exchange.getRequestURI().getPath());
                json.add("data", route.execute(exchange));
            } catch (Exception e) {
                responseCode = 500;
                json.addProperty("error", e.getMessage());
            }
        } else {
            responseCode = 404;
            json.addProperty("error", "Not Found");
        }

        json.addProperty("code", responseCode);

        headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("Access-Control-Allow-Origin", "*");
        final byte[] rawResponse = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, rawResponse.length);
        exchange.getResponseBody().write(rawResponse);
        exchange.close();
    }

    public void addRoute(String path, Route route) {
        routes.put(path, route);
    }
}
