package flustix.fluxifyed.utils;

public class MessageUtils {
    public static String exceptionToCode(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("```java\n");
        sb.append(e.getMessage());

        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\n    ");
            sb.append(element.toString());
        }

        sb.append("\n```");
        return sb.toString();
    }
}
