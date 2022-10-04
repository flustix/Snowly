package flustix.fluxifyed.utils.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JSONUtils {
    public static JsonObject toJson(Object object) {
        return new GsonBuilder().create().toJsonTree(object).getAsJsonObject();
    }
}
