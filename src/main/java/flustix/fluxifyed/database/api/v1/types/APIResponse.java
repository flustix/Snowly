package flustix.fluxifyed.database.api.v1.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import flustix.fluxifyed.utils.JSONUtils;

public class APIResponse {
    public final int code;
    public final String message;
    public final JsonElement data;

    public APIResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;

        if (data instanceof JsonElement)
            this.data = (JsonElement) data;
        else if (data == null)
            this.data = new JsonObject();
        else
            this.data = JSONUtils.toJson(data);
    }
}
