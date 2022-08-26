package flustix.fluxifyed.api;

import com.sun.net.httpserver.HttpServer;
import flustix.fluxifyed.api.routes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIServer {
    public static Logger LOGGER = LoggerFactory.getLogger("FluxifyedAPI");

    public static void main() throws Exception {
        Router router = new Router();

        router.addRoute("/guilds", new GuildsRoute());
        router.addRoute("/commands", new CommandsRoute());

        int port = 8080;

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", router);
        server.setExecutor(null);
        server.start();

        LOGGER.info("API Server started on port {}", port);
    }
}
