package flustix.fluxifyed.modules.timers.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.message.MessageData;
import flustix.fluxifyed.utils.CustomMessageUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimedMessage {
    private final String guildId;
    private final String channelId;
    private final String message;
    private final JsonArray random;
    private final String time;
    private final int antiRepeat;
    private final List<Integer> lastIndexes = new ArrayList<>();

    private boolean sent = false;

    public TimedMessage(String guildId, String channelId, String message, String time, String random, int antiRepeat, String lastIndexes) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.message = message;
        this.time = time;
        this.random = JsonParser.parseString(random).getAsJsonArray();
        this.antiRepeat = antiRepeat;
        for (String s : lastIndexes.split(",")) {
            if (!s.isEmpty()) {
                this.lastIndexes.add(Integer.parseInt(s));
            }
        }
    }

    public void send() {
        send(false);
    }

    public void send(boolean force) {
        if (!force) {
            Calendar calendar = Calendar.getInstance();
            String[] timeSplit = time.split(":");
            int hour = Integer.parseInt(timeSplit[0]);
            int minute = Integer.parseInt(timeSplit[1]);

            if (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) == minute) {
                if (sent) return;
                sent = true;
            } else {
                sent = false;
                return;
            }
        }


        TextChannel channel = Main.getBot().getTextChannelById(channelId);
        if (channel == null) return;

        String message = this.message;
        if (random.size() > 0) {
            int index = (int) (Math.random() * random.size());
            if (antiRepeat > 0) {
                if (random.size() < antiRepeat) {
                    lastIndexes.clear();
                } else {
                    while (lastIndexes.contains(index)) {
                        index = (int) (Math.random() * random.size());
                    }
                }

                if (lastIndexes.size() >= antiRepeat) {
                    lastIndexes.remove(0);
                }

                lastIndexes.add(index);
            }

            JsonObject randomEntry = random.get(index).getAsJsonObject();

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
