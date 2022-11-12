package flustix.fluxifyed.database.api.v1;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import flustix.fluxifyed.database.api.APIServer;
import flustix.fluxifyed.database.api.v1.types.APIResponse;
import flustix.fluxifyed.database.api.v1.types.APIRoute;
import flustix.fluxifyed.database.api.v1.types.Route;
import org.reflections.Reflections;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Router implements HttpHandler {
    private static final HashMap<String, Route> routes = new HashMap<>();

    public void init() {
        Reflections reflections = new Reflections("flustix.fluxifyed.database.api.v1.routes");
        reflections.getTypesAnnotatedWith(APIRoute.class).forEach(clazz -> {
            try {
                APIRoute annotation = clazz.getAnnotation(APIRoute.class);
                addRoute(annotation.method() + "|" + annotation.path(), (Route) clazz.getConstructor().newInstance());
            } catch (Exception e) {
                APIServer.LOGGER.error("Error while loading route: " + clazz.getName());
                e.printStackTrace();
            }
        });
    }

    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();

        JsonObject json = new JsonObject();
        json.addProperty("code", 0); // do this first so it is always at the top

        boolean notFound = true;

        if (exchange.getRequestMethod().equals("HEAD")) {
            return; // don't do anything if it's a HEAD request
        } else {
            for (Map.Entry<String, Route> routeEntry : routes.entrySet()) {
                String[] route = routeEntry.getKey().split("\\|");
                String method = route[0];
                String path = route[1];

                if (!exchange.getRequestMethod().equals(method)) {
                    continue;
                }

                String[] pathSplit = path.split("/");
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
                            APIResponse response = routeEntry.getValue().execute(exchange, params);
                            json.addProperty("code", response.code);
                            json.addProperty("message", response.message);
                            json.add("data", response.data);

                        } catch (Exception e) {
                            json.addProperty("code", 500);
                            json.addProperty("error", e.getMessage());
                        }
                        break;
                    }
                }
            }
        }

        if (notFound) {
            json.addProperty("code", 404);
            json.addProperty("error", "Not Found");
        }

        headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        final byte[] rawResponse = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, rawResponse.length);
        exchange.getResponseBody().write(rawResponse);
        exchange.close();
    }

    public void addRoute(String path, Route route) {
        routes.put(path, route);
    }
}
