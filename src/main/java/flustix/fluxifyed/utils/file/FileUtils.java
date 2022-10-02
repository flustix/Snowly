package flustix.fluxifyed.utils.file;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String getResourceString(String path) {
        try {
            return Files.readString(Paths.get(FileUtils.class.getResource(path).toURI()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
