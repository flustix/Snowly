package flustix.fluxifyed.database.api.v1;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import flustix.fluxifyed.database.api.v1.types.Route;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Router implements HttpHandler {
    private static final HashMap<String, Route> routes = new HashMap<>();

    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        int responseCode = 200;

        JsonObject json = new JsonObject();
        json.addProperty("code", 0); // do this first so it is always at the top

        boolean notFound = true;

        if (exchange.getRequestMethod().equals("HEAD")) {
            return; // don't do anything if it's a HEAD request
        } else {
            for (Map.Entry<String, Route> routeEntry : routes.entrySet()) {
                String[] pathSplit = routeEntry.getKey().split("/");
                String[] requestSplit = exchange.getRequestURI().getPath().split("/");

                if (pathSplit.length == requestSplit.length) {
                    boolean match = true;
                    HashMap<String, String> params = new HashMap<>();

                    for (int i = 0; i < pathSplit.length; i++) {
                        if (pathSplit[i].startsWith(":")) {
                            params.put(pathSplit[i].substring(1), requestSplit[i]);
                            continue;
                        }
                        if (!pathSplit[i].equals(requestSplit[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        notFound = false;
                        try {
                            json.add("data", routeEntry.getValue().execute(exchange, params));
                        } catch (Exception e) {
                            responseCode = 500;
                            json.addProperty("error", e.getMessage());
                        }
                        break;
                    }
                }
            }
        }

        if (notFound) {
            responseCode = 404;
            json.addProperty("error", "Not Found");
        }

        json.addProperty("code", responseCode);

        headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("Access-Control-Allow-Origin", "*");
        final byte[] rawResponse = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, rawResponse.length);
        exchange.getResponseBody().write(rawResponse);
        exchange.close();
    }

    public void addRoute(String path, Route route) {
        routes.put(path, route);
    }
}
