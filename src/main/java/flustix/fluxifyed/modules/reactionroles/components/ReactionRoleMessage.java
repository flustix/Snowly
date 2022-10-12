package flustix.fluxifyed.modules.reactionroles.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flustix.fluxifyed.Main;
import flustix.fluxifyed.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.HashMap;
import java.util.Objects;

public class ReactionRoleMessage {
    final HashMap<String, ReactionRole> roles = new HashMap<>();
    final String messageid;
    String title;

    public ReactionRoleMessage(String messageid, String data) {
        this.messageid = messageid;

        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        title = json.get("name").getAsString();
        json.getAsJsonArray("roles").forEach(element -> {
            JsonObject role = element.getAsJsonObject();

            String emoji = role.get("emoji").getAsString();
            emoji = emoji.replace("<:", "").replace("<a:", "").replace(">", "");

            roles.put(emoji, new ReactionRole(role.get("emoji").getAsString(), role.get("roleid").getAsString(), role.get("name").getAsString(), role.get("description").getAsString()));
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addRole(String emoji, String roleid, String name, String description) {
        roles.put(emoji, new ReactionRole(emoji, roleid, name, description));
    }

    public boolean update(SlashCommandInteraction interaction) {
        JsonObject json = new JsonObject();
        json.addProperty("name", title);

        JsonArray jsonRoles = new JsonArray();
        this.roles.forEach((emoji, role) -> {
            JsonObject roleJson = new JsonObject();
            roleJson.addProperty("emoji", role.emoji);
            roleJson.addProperty("roleid", role.roleid);
            roleJson.addProperty("name", role.name);
            roleJson.addProperty("description", role.description);
            jsonRoles.add(roleJson);
        });

        json.add("roles", jsonRoles);

        Database.executeQuery("UPDATE reactionRoles SET data = '" + json + "' WHERE messageid = " + messageid);

        // message
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(Main.accentColor);
        embed.setDescription("React to this message to get a role!");

        roles.forEach((emoji, role) -> embed.addField(role.emoji + " " + role.name, role.description, true));

        try {
            interaction.getChannel().retrieveMessageById(messageid).queue((message) -> message.editMessageEmbeds(embed.build()).queue());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            interaction.reply("Something went wrong!").setEphemeral(true).queue();
            return false;
        }
    }

    public void onReactionAdd(MessageReactionAddEvent event) {
        String emoteString = event.getReaction().getEmoji().getAsReactionCode();

        ReactionRole role = roles.get(emoteString);
        if (role == null) return;

        try {
            Member member = event.getMember();
            Role r = event.getGuild().getRoleById(role.roleid);
            if (member == null || r == null) return;
            event.getGuild().addRoleToMember(member, r).complete();
        } catch (Exception e) {
            // dm guild owner or smth
        }
    }

    public void onReactionRemove(MessageReactionRemoveEvent event) {
        String emoteString = event.getReaction().getEmoji().getAsReactionCode();

        ReactionRole role = roles.get(emoteString);
        if (role == null) return;

        try {
            Member member = event.getGuild().getMemberById(event.getUserId());
            Role r = event.getGuild().getRoleById(role.roleid);
            if (member == null || r == null) return;

            event.getGuild().removeRoleFromMember(member, r).complete();
        } catch (Exception e) {
            // dm guild owner or smth
        }
    }
}
