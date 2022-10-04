package flustix.fluxifyed.utils.file;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtils {
    public static String getResourceString(String path) {
        try {
            return new Scanner(FileUtils.class.getResourceAsStream(path), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        } catch (Exception e) {
            return "";
        }
    }
}
