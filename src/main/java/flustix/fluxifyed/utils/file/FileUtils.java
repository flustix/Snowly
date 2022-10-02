package flustix.fluxifyed.utils.file;

import java.util.Scanner;
public class FileUtils {
    public static String getResourceString(String path) {
        try {
            return new Scanner(FileUtils.class.getResourceAsStream(path), "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            return "";
        }
    }
}
