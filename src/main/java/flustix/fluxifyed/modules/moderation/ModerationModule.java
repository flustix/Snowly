package flustix.fluxifyed.modules.moderation;

import flustix.fluxifyed.Main;
import flustix.fluxifyed.components.Module;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.modules.moderation.automod.AutoMod;
import flustix.fluxifyed.modules.moderation.components.AutoAction;
import flustix.fluxifyed.modules.moderation.components.Infraction;
import flustix.fluxifyed.modules.moderation.types.AutoActionType;
import flustix.fluxifyed.modules.moderation.types.InfractionType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModerationModule extends Module {
    private static List<Infraction> infractions;
    private static HashMap<String, List<AutoAction>> autoActions;

    public ModerationModule() {
        super("moderation", "Moderation", "Moderate your server with ease.");
    }

    public void init() {
        loadInfractions();
        loadAutoActions();
    }

    public void onGuildInit(Guild guild) {
        AutoMod.loadGuild(guild);
    }

    private void loadInfractions() {
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

    private static void loadAutoActions() {
        autoActions = new HashMap<>();

        ResultSet rs = Database.executeQuery("SELECT * FROM fluxifyed.autoactions");
        if (rs == null) {
            Main.LOGGER.error("Error while retrieving auto actions from database: ResultSet is null");
            return;
        }

        try {
            while (rs.next()) {
                AutoAction action = new AutoAction(
                        AutoActionType.fromString(rs.getString("action")),
                        rs.getInt("duration"),
                        rs.getInt("infractions"),
                        rs.getInt("threshold")
                );

                if (!autoActions.containsKey(rs.getString("id"))) {
                    autoActions.put(rs.getString("id"), new ArrayList<>());
                }
                autoActions.get(rs.getString("id")).add(action);

                Main.LOGGER.info("ID: " + rs.getString("id") + " | Action: " + action.getAction().toString() + " | Duration: " + action.getDuration() + " | Threshold: " + rs.getInt("threshold"));
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error while retrieving auto actions from database", e);
        }
    }

    public static void addInfraction(Infraction infraction) {
        infraction.addToDatabase();
        infractions.add(infraction);

        List<AutoAction> actions = autoActions.get(infraction.getGuild());
        if (actions == null) return;

        List<Infraction> userInfractions = infractions.stream()
                .filter(i -> i.getGuild().equals(infraction.getGuild()) && i.getUser().equals(infraction.getUser()) && i.getType() != InfractionType.NOTE)
                .toList();

        // all actions that apply to this infraction sorted by infraction count (descending)
        List<AutoAction> matchingActions = actions.stream()
                .filter(action -> action.canTrigger(userInfractions))
                .sorted((a, b) -> b.getInfractions() - a.getInfractions())
                .toList();

        if (matchingActions.isEmpty()) return;

        AutoAction action = matchingActions.get(0);
        Guild guild = Main.getBot().getGuildById(infraction.getGuild());
        if (guild == null) return;
        Member member = guild.getMemberById(infraction.getUser());
        if (member == null) {
            try {
                member = guild.retrieveMemberById(infraction.getUser()).complete();
            } catch (Exception e) {
                Main.LOGGER.error("Error while retrieving member from guild", e);
                return;
            }
        }

        switch (action.getAction()) {
            case Kick -> guild.kick(member).queue();
            case Timeout -> member.timeoutFor(action.getDuration(), TimeUnit.SECONDS).queue();
            case Mute -> { } // we don't have a mute system yet
            case Ban -> guild.ban(member, 0, TimeUnit.SECONDS).queue();
        }
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
