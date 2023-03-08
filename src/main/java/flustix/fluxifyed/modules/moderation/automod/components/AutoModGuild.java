package flustix.fluxifyed.modules.moderation.automod.components;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.moderation.ModerationModule;
import flustix.fluxifyed.modules.moderation.automod.components.messages.MessageStorage;
import flustix.fluxifyed.modules.moderation.automod.types.AutoModRuleset;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoModGuild {
    public final String id;

    private AutoModRuleset badWordsRuleset = AutoModRuleset.Nothing;
    private AutoModRuleset massMentionsRuleset = AutoModRuleset.Nothing;
    private AutoModRuleset massEmojisRuleset = AutoModRuleset.Nothing;
    private AutoModRuleset massCapsRuleset = AutoModRuleset.Nothing;
    private AutoModRuleset spamRuleset = AutoModRuleset.Nothing;

    private final ArrayList<String> badWords = new ArrayList<>();
    private int massMentionsThreshold = 7;
    private int massEmojisThreshold = 7;
    private int massCapsThreshold = 80;
    private int spamThreshold = 3;

    public AutoModGuild(String id) {
        this.id = id;
        load();
    }

    private void load() {
        ResultSet rs = Database.executeQuery("SELECT * FROM fluxifyed.automod WHERE id = '?'", id);
        if (rs == null) return;

        try {
            while (rs.next()) {
                String rulesetsJson = rs.getString("rulesets");
                String settingsJson = rs.getString("settings");

                JsonObject rulesets = JsonParser.parseString(rulesetsJson).getAsJsonObject();
                JsonObject settings = JsonParser.parseString(settingsJson).getAsJsonObject();

                // Rulesets
                if (rulesets.has("badwords"))
                    badWordsRuleset = AutoModRuleset.parse(rulesets.get("badwords").getAsInt());
                if (rulesets.has("massmentions"))
                    massMentionsRuleset = AutoModRuleset.parse(rulesets.get("massmentions").getAsInt());
                if (rulesets.has("massemojis"))
                    massEmojisRuleset = AutoModRuleset.parse(rulesets.get("massemojis").getAsInt());
                if (rulesets.has("masscaps"))
                    massCapsRuleset = AutoModRuleset.parse(rulesets.get("masscaps").getAsInt());
                if (rulesets.has("spam"))
                    spamRuleset = AutoModRuleset.parse(rulesets.get("spam").getAsInt());

                // Settings
                if (settings.has("badwords"))
                    settings.getAsJsonArray("badwords").forEach(e -> badWords.add(e.getAsString()));
                if (settings.has("massmentions"))
                    massMentionsThreshold = settings.get("massmentions").getAsInt();
                if (settings.has("massemojis"))
                    massEmojisThreshold = settings.get("massemojis").getAsInt();
                if (settings.has("masscaps"))
                    massCapsThreshold = settings.get("masscaps").getAsInt();
                if (settings.has("spam"))
                    spamThreshold = settings.get("spam").getAsInt();
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while loading AutoMod settings for guild " + id, e);
        }
    }

    public void checkMessage(Message message) {
        // bad words
        for (String word : badWords) {
            if (message.getContentRaw().contains(word)) {
                Infraction warn = new Infraction(id, message.getAuthor().getId(), message.getJDA().getSelfUser().getId(), "warn", "Bad word: " + word, message.getTimeCreated().toInstant().toEpochMilli());
                AuditableRestAction<Void> deleteAction = message.delete().reason("Bad word usage: " + word);

                switch (badWordsRuleset) {
                    case Nothing:
                        break;
                    case Delete:
                        deleteAction.queue();
                        break;
                    case Warn:
                        ModerationModule.addInfraction(warn);
                        break;
                    case WarnDelete:
                        ModerationModule.addInfraction(warn);
                        deleteAction.queue();
                        break;
                }
            }
        }

        // mass mentions
        if (message.getMentions().getUsersBag().size() >= massMentionsThreshold) {
            Infraction warn = new Infraction(id, message.getAuthor().getId(), message.getJDA().getSelfUser().getId(), "warn", "Mass mentions", message.getTimeCreated().toInstant().toEpochMilli());
            AuditableRestAction<Void> deleteAction = message.delete().reason("Mass mentions");

            switch (massMentionsRuleset) {
                case Nothing:
                    break;
                case Delete:
                    deleteAction.queue();
                    break;
                case Warn:
                    ModerationModule.addInfraction(warn);
                    break;
                case WarnDelete:
                    ModerationModule.addInfraction(warn);
                    deleteAction.queue();
                    break;
            }
        }

        // mass emojis
        if (message.getMentions().getCustomEmojisBag().size() >= massEmojisThreshold) {
            Infraction warn = new Infraction(id, message.getAuthor().getId(), message.getJDA().getSelfUser().getId(), "warn", "Mass emojis", message.getTimeCreated().toInstant().toEpochMilli());
            AuditableRestAction<Void> deleteAction = message.delete().reason("Mass emojis");

            switch (massEmojisRuleset) {
                case Nothing:
                    break;
                case Delete:
                    deleteAction.queue();
                    break;
                case Warn:
                    ModerationModule.addInfraction(warn);
                    break;
                case WarnDelete:
                    ModerationModule.addInfraction(warn);
                    deleteAction.queue();
                    break;
            }
        }

        // mass caps
        int caps = 0;

        for (char c : message.getContentRaw().toCharArray())
            if (Character.isUpperCase(c)) caps++;

        float percent = (float) caps / message.getContentRaw().length() * 100;

        if (percent >= massCapsThreshold) {
            Infraction warn = new Infraction(id, message.getAuthor().getId(), message.getJDA().getSelfUser().getId(), "warn", "Mass caps", message.getTimeCreated().toInstant().toEpochMilli());
            AuditableRestAction<Void> deleteAction = message.delete().reason("Mass caps");

            switch (massCapsRuleset) {
                case Nothing:
                    break;
                case Delete:
                    deleteAction.queue();
                    break;
                case Warn:
                    ModerationModule.addInfraction(warn);
                    break;
                case WarnDelete:
                    ModerationModule.addInfraction(warn);
                    deleteAction.queue();
                    break;
            }
        }

        // spam
        List<Message> matches = new ArrayList<>();

        for (Message msg : MessageStorage.getByAuthorInGuild(message.getAuthor().getId(), id)) {
            if (msg.getContentRaw().equals(message.getContentRaw())) {
                if (msg.getTimeCreated().toInstant().toEpochMilli() + TimeUnit.MINUTES.toMillis(2) >= message.getTimeCreated().toInstant().toEpochMilli()) {
                    matches.add(msg);
                }
            }
        }

        if (matches.size() >= spamThreshold) {
            if (spamRuleset != AutoModRuleset.DeleteTimeout) return;

            for (Message match : matches) match.delete().queue(s -> {}, e -> {
                if (e instanceof ErrorResponseException error) {
                    if (error.getErrorCode() == 10008) return; // message already deleted
                    Main.LOGGER.error("Error while deleting message", e);
                }
            });

            Member member = message.getMember();

            if (member == null) {
                Main.LOGGER.warn("Member intent disabled!");
                return;
            }

            message.getMember().timeoutFor(1, TimeUnit.HOURS).reason("Spam").queue();
        }
    }
}
