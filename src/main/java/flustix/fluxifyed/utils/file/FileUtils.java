package flustix.fluxifyed.utils.file;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtils {
    public static String getResourceString(String path) {
        try {
            InputStream stream = FileUtils.class.getResourceAsStream(path);
            if (stream == null) return "";

            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            return "";
        }
    }
}
