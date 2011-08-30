package net.res0l.openra;

public class Size {
	public int Width, Height;
	
	public Size(int width, int height) {
		this.Width = width;
		this.Height = height;
	}
	
	public Size(float width, float height)
	{
		this((int)width,(int)height);
	}
	
	public Size mul(float scale)
	{
		return new Size(Width * scale, Height * scale);
	}
}
