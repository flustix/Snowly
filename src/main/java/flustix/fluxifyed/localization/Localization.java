package flustix.fluxifyed.localization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Localization {
    private static final Logger LOGGER = LoggerFactory.getLogger("Localization");
    private static final HashMap<DiscordLocale, HashMap<String, String>> locales = new HashMap<>();

    public static void init() {
        LOGGER.info("Loading localization files...");

        URL url = Localization.class.getResource("/lang");
        if (url == null) {
            LOGGER.error("Could not find the lang folder!");
            return;
        }

        File langFolder = new File(url.getPath());
        if (!langFolder.isDirectory()) {
            LOGGER.error("The lang folder is not a directory!");
            return;
        }

        File[] languageFolders = langFolder.listFiles();
        if (languageFolders == null) {
            LOGGER.error("No language folders found!");
            return;
        }

        for (File lang : languageFolders) {
            if (!lang.isDirectory()) {
                LOGGER.warn("Found a file in the lang folder: " + lang.getName());
                continue;
            }

            DiscordLocale locale = DiscordLocale.from(lang.getName());
            if (locale == DiscordLocale.UNKNOWN) {
                LOGGER.error("Unknown locale: " + lang.getName());
                continue;
            }

            HashMap<String, String> strings = new HashMap<>();

            File[] files = lang.listFiles();
            if (files == null) {
                LOGGER.warn("No files found in the " + lang.getName() + " folder!");
                continue;
            }

            for (File file : files) {
                if (!file.getName().endsWith(".json")) {
                    LOGGER.warn("Found a file in the " + lang.getName() + " folder that is not a JSON file: " + file.getName());
                    continue;
                }

                try {
                    JsonObject json = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();

                    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                        String key = entry.getKey();
                        JsonElement element = entry.getValue();

                        if (!element.isJsonPrimitive()) {
                            LOGGER.warn("The value of the key " + key + " in the file " + file.getName() + " is not a primitive!");
                            continue;
                        }

                        String value = element.getAsString();

                        if (value.isEmpty()) {
                            LOGGER.warn("The value of the key " + key + " is empty!");
                            continue;
                        }

                        strings.put(key, value);
                    }
                } catch (Exception e) {
                    LOGGER.error("Could not read the file: " + file.getName() + " in the " + lang.getName() + " folder!", e);
                }
            }

            locales.put(locale, strings);
            LOGGER.info("Loaded language: " + locale + "!");
        }
    }

    public static String get(String key) {
        return get(DiscordLocale.ENGLISH_US, key);
    }

    public static String get(DiscordLocale locale, String key) {
        HashMap<String, String> strings = locales.get(locale);
        if (strings == null) {
            LOGGER.warn("Could not find the strings for the locale " + locale + "!");
            return key;
        }

        String value = strings.get(key);
        if (value == null) {
            LOGGER.warn("Could not find the key " + key + " in the locale " + locale + "!");
            return key;
        }

        return value;
    }

    public static HashMap<DiscordLocale, String> getAll(String key) {
        HashMap<DiscordLocale, String> values = new HashMap<>();

        for (Map.Entry<DiscordLocale, HashMap<String, String>> entry : locales.entrySet()) {
            DiscordLocale locale = entry.getKey();
            HashMap<String, String> strings = entry.getValue();

            String value = strings.get(key);
            if (value == null) {
                LOGGER.warn("Could not find the key " + key + " in the locale " + locale + "!");
                value = key;
            }

            values.put(locale, value);
        }

        return values;
    }
}
