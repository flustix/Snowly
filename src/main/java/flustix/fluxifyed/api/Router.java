package flustix.fluxifyed.api;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Router implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        String response = "";
        int responseCode = 200;

        response = "{}";

        headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        headers.set("Access-Control-Allow-Origin", "*");
        final byte[] rawResponse = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, rawResponse.length);
        exchange.getResponseBody().write(rawResponse);
        exchange.close();
    }
}
