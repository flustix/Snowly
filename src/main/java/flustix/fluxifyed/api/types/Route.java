package flustix.fluxifyed.api.types;

import com.google.gson.JsonObject;

public interface Route {
    JsonObject execute() throws Exception;
}
