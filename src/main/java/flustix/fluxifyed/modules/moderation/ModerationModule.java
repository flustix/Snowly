package flustix.fluxifyed.modules.moderation;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import flustix.fluxifyed.modules.moderation.types.InfractionType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModerationModule extends Module {
    private static List<Infraction> infractions;

    public ModerationModule() {
        super("moderation", "Moderation", "Moderate your server with ease.");
    }

    public void init() {
        infractions = new ArrayList<>();

        ResultSet rs = Database.executeQuery("SELECT * FROM fluxifyed.infractions");
        if (rs == null) {
            Main.LOGGER.error("Error while retrieving infractions from database: ResultSet is null");
            return;
        }

        try {
            while (rs.next()) {
                Infraction infraction = new Infraction(
                        rs.getString("guildid"),
                        rs.getString("userid"),
                        rs.getString("modid"),
                        rs.getString("type"),
                        rs.getString("content"),
                        rs.getLong("time")
                );
                infraction.setId(rs.getInt("id"));
                infractions.add(infraction);
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while retrieving infractions from database", e);
        }
    }

    public static void addInfraction(Infraction infraction) {
        infraction.addToDatabase();
        infractions.add(infraction);
    }

    public static List<Infraction> getInfractions() {
        return infractions;
    }

    public static List<Infraction> getInfractions(String guild) {
        return infractions.stream().filter(infraction -> infraction.getGuild().equals(guild)).collect(Collectors.toList());
    }

    public static List<Infraction> getInfractions(String guild, String user) {
        return infractions.stream().filter(infraction -> infraction.getGuild().equals(guild) && infraction.getUser().equals(user)).collect(Collectors.toList());
    }

    public static List<Infraction> getInfractions(String guild, String user, InfractionType type) {
        return infractions.stream().filter(infraction -> infraction.getGuild().equals(guild) && infraction.getUser().equals(user) && infraction.getType().equals(type)).collect(Collectors.toList());
    }

    public static Infraction getInfraction(int id) {
        return infractions.stream().filter(infraction -> infraction.getId() == id).findFirst().orElse(null);
    }
}
