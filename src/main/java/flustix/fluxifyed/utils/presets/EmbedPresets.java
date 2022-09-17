package flustix.fluxifyed.utils.presets;

import flustix.fluxifyed.Main;
import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedPresets {
    public static final EmbedBuilder loading = new EmbedBuilder()
                .setAuthor("Loading...", null, Main.getBot().getSelfUser().getAvatarUrl())
                .setDescription("Please wait...")
                .setColor(Main.accentColor);
}
