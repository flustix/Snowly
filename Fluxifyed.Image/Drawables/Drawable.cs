﻿namespace Fluxifyed.Image.Drawables; 

public abstract class Drawable {
    public int X { get; set; } = 0;
    public int Y { get; set; } = 0;
    public int Width { get; set; } = 1;
    public int Height { get; set; } = 1;
    public int CornerRadius { get; set; } = 0;
    public Argb32 Color { get; set; } = new(255, 255, 255);
    
    public abstract void Draw(Image<Argb32> image);
}