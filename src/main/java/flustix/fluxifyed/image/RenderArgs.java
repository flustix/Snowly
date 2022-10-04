package flustix.fluxifyed.image;

public class RenderArgs {
    public final String template;
    public final String output;
    public final RenderData data;

    public RenderArgs(String template, String output, RenderData data) {
        this.template = template;
        this.output = output;
        this.data = data;
    }
}
