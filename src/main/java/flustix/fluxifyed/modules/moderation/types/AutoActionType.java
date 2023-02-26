package flustix.fluxifyed.modules.moderation.types;

public enum AutoActionType {
    Kick,
    Timeout,
    Mute,
    Ban;

    public static AutoActionType fromString(String type) {
        return switch (type) {
            case "kick" -> Kick;
            case "timeout" -> Timeout;
            case "mute" -> Mute;
            case "ban" -> Ban;
            default -> null;
        };
    }
}
