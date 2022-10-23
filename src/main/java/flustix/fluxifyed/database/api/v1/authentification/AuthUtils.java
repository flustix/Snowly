package flustix.fluxifyed.database.api.v1.authentification;

import com.sun.net.httpserver.Headers;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import flustix.fluxifyed.database.api.v1.components.APIGuild;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuthUtils {
    public static String getToken(Headers headers) {
        List<String> tokenHeader = headers.get("Authorization");

        if (tokenHeader == null) return "";

        return tokenHeader.get(0);
    }

    public static String getUserId(String token) {
        ResultSet rs = Database.executeQuery("SELECT userid FROM tokens WHERE token = '" + token + "'");
        if (rs == null) return "";

        try {
            if (rs.next())
                return rs.getString("userid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static List<Guild> getServers(String userid) {
        List<Guild> ids = new ArrayList<>();

        Main.getBot().getGuilds().forEach(guild -> {
            try {
                Member member = guild.retrieveMemberById(userid).complete();
                if (member == null) member = guild.retrieveMemberById(userid).complete();

                if (member.hasPermission(Permission.ADMINISTRATOR))
                    ids.add(guild);
            } catch (Exception ignored) {
            }
        });

        return ids;
    }

    public static APIGuild getGuild(String userid, String guildid) {
        Guild guild = Main.getBot().getGuildById(guildid);
        if (guild == null) return null;

        Member member = guild.retrieveMemberById(userid).complete();
        if (member == null) member = guild.retrieveMemberById(userid).complete();

        if (member.hasPermission(Permission.ADMINISTRATOR))
            return new APIGuild(guild);

        return null;
    }
}
