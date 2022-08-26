package flustix.fluxifyed.api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIServer {
    public static Logger LOGGER = LoggerFactory.getLogger("FluxifyedAPI");

    public static void main() throws Exception {
        Router router = new Router();

        router.addRoute("/hello", () -> {
            JsonObject json = new JsonObject();
            json.addProperty("message", "Hello World!");
            return json;
        });

        router.addRoute("/error", () -> {
            throw new Exception("Test Exception");
        });

        int port = 8080;

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", router);
        server.setExecutor(null);
        server.start();

        LOGGER.info("API Server started on port {}", port);
    }
}
