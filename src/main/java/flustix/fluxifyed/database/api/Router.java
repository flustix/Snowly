package flustix.fluxifyed.database.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import flustix.fluxifyed.database.api.types.APIResponse;
import flustix.fluxifyed.database.api.types.APIRoute;
import flustix.fluxifyed.database.api.types.Route;
import flustix.fluxifyed.database.api.utils.ratelimit.RateLimitData;
import flustix.fluxifyed.database.api.utils.ratelimit.RateLimitUtils;
import org.reflections.Reflections;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Router implements HttpHandler {
    private static final HashMap<String, Route> routes = new HashMap<>();
    private static final HashMap<String, Integer> rateLimits = new HashMap<>();

    public void init() {
        Reflections reflections = new Reflections("flustix.fluxifyed.database.api.routes");
        reflections.getTypesAnnotatedWith(APIRoute.class).forEach(clazz -> {
            try {
                APIRoute annotation = clazz.getAnnotation(APIRoute.class);
                addRoute(annotation.method() + "|" + annotation.path(), (Route) clazz.getConstructor().newInstance());
                rateLimits.put(annotation.path(), annotation.rateLimit());
            } catch (Exception e) {
                APIServer.LOGGER.error("Error while loading route: " + clazz.getName());
                e.printStackTrace();
            }
        });
    }

    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("HEAD")) {
            return;
        }

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
                    sendResponse(exchange, path, routeEntry.getValue(), params, true);
                    return;
                }
            }
        }

        sendResponse(exchange, null, null, null, false);
    }

    private void sendResponse(HttpExchange exchange, String path, Route route, HashMap<String, String> params, boolean found) throws IOException {
        Headers headers = exchange.getResponseHeaders();

        JsonObject json = new JsonObject();
        json.addProperty("code", 0); // do this first so it is always at the top

        if (found) {
            String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
            int rateLimit = rateLimits.getOrDefault(path, 10);
            RateLimitData rateLimitData = RateLimitUtils.isRateLimited(ip, path, rateLimit);

            if (rateLimitData.requestsLeft != 0) {
                RateLimitUtils.addRateLimit(ip, path);

                try {
                    APIResponse response = route.execute(exchange, params);
                    json.addProperty("code", response.code);
                    json.addProperty("message", response.message);
                    json.add("data", response.data);
                    json.addProperty("requestsLeft", rateLimitData.requestsLeft);
                } catch (Exception e) {
                    json.addProperty("code", 500);
                    json.addProperty("message", "Something went very wrong >-<'. Please report this to the developer.");
                    json.addProperty("error", e.toString());

                    JsonArray stackTrace = new JsonArray();
                    for (StackTraceElement element : e.getStackTrace()) {
                        stackTrace.add(element.toString());
                    }
                    json.add("stack", stackTrace);
                }
            } else {
                json.addProperty("code", 429);
                json.addProperty("message", "You are being rate limited. Please wait a bit before trying again.");
                json.addProperty("seconds", rateLimitData.secondsLeft);
            }
        } else {
            json.addProperty("code", 404);
            json.addProperty("message", "We couldn't find the route you were looking for. T^T");
        }

        headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        setSecurityHeaders(headers);
        final byte[] rawResponse = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, rawResponse.length);
        exchange.getResponseBody().write(rawResponse);
        exchange.close();
    }

    public void addRoute(String path, Route route) {
        routes.put(path, route);
    }

    private void setSecurityHeaders(Headers headers) {
        headers.set("Content-Security-Policy", "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'; connect-src 'self';");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("X-Frame-Options", "DENY");
        headers.set("X-XSS-Protection", "1; mode=block");
        headers.set("Referrer-Policy", "no-referrer");
        headers.set("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }

    public static HashMap<String, Integer> getRateLimits() {
        return rateLimits;
    }

    public static Integer getRateLimit(String path) {
        return rateLimits.get(path);
    }
}
