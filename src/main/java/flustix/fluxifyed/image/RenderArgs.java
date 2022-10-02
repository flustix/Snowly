package flustix.fluxifyed.image;

public class RenderArgs {
    public String template;
    public String output;
    public RenderData data;

    public RenderArgs(String template, String output, RenderData data) {
        this.template = template;
        this.output = output;
        this.data = data;
    }
}
