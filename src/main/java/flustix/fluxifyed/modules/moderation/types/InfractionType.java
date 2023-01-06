package flustix.fluxifyed.modules.moderation.types;

public enum InfractionType {
    NOTE,
    WARN,
    MUTE,
    KICK,
    BAN;

    public static InfractionType fromString(String type) {
        return switch (type) {
            case "note" -> NOTE;
            case "warn" -> WARN;
            case "mute" -> MUTE;
            case "kick" -> KICK;
            case "ban" -> BAN;
            default -> null;
        };
    }

    public String toString() {
        return super.toString().toLowerCase();
    }
}
