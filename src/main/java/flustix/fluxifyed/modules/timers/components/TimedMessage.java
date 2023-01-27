package flustix.fluxifyed.modules.timers.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.utils.CustomMessageUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Calendar;
import java.util.Map;

public class TimedMessage {
    private final String guildId;
    private final String channelId;
    private final String message;
    private final JsonArray random;
    private final String time;

    public TimedMessage(String guildId, String channelId, String message, String time, String random) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.message = message;
        this.time = time;
        this.random = JsonParser.parseString(random).getAsJsonArray();
    }

    public void send() {
        Calendar calendar = Calendar.getInstance();
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);
        int second = Integer.parseInt(timeSplit[2]);

        if (calendar.get(Calendar.HOUR_OF_DAY) != hour && calendar.get(Calendar.MINUTE) != minute && calendar.get(Calendar.SECOND) != second)
            return;

        TextChannel channel = Main.getBot().getTextChannelById(channelId);
        if (channel == null) return;

        String message = this.message;
        if (random.size() > 0) {
            JsonObject randomEntry = random.get((int) (Math.random() * random.size())).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : randomEntry.entrySet()) {
                JsonElement value = entry.getValue();
                String valueString = value != null ? value.getAsString() : "null";
                message = message.replace("{" + entry.getKey() + "}", valueString);
            }
        }

        MessageData messageData = CustomMessageUtils.parseMessage(message);
        if (messageData == null) return;

        channel.sendMessage(CustomMessageUtils.create(messageData)).queue();
    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }
}
