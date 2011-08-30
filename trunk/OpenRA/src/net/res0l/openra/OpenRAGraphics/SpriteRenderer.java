package net.res0l.openra.OpenRAGraphics;

import com.badlogic.gdx.math.Vector2;

import net.res0l.openra.Size;
import net.res0l.openra.OpenRAFileFormat.Vertex;
import net.res0l.openra.OpenRAFileFormat.IGraphicsDevice.*;

public class SpriteRenderer implements Renderer.IBatchRenderer
{
	Renderer renderer;
	IShader shader;

	Vertex[] vertices = new Vertex[Renderer.TempBufferSize];
	long[] indices = new long[Renderer.TempBufferSize];
	int nv = 0, ni = 0;

	public SpriteRenderer(Renderer renderer, IShader shader)
	{
		this.renderer = renderer;
		this.shader = shader;
	}
			
	public void DrawSprite(OpenRASprite s, Vector2 location, WorldRenderer wr, String palette)
	{
		DrawSprite(s, location, wr.GetPaletteIndex(palette), s.size);
	}
	
	public void DrawSprite(OpenRASprite s, Vector2 location, WorldRenderer wr, String palette, Size size)
	{
		DrawSprite(s, location, wr.GetPaletteIndex(palette), size);
	}
	
	public void DrawSprite(OpenRASprite s, Vector2 location, int paletteIndex, Size size)
	{
		Renderer.batch.begin();
		s.draw(Renderer.batch);
		Renderer.batch.end();
	}
	
	// For RGBASpriteRenderer, which doesn't use palettes
	public void DrawSprite(OpenRASprite s, Vector2 location)
	{
		DrawSprite(s, location, 0, s.size);
	}
	
	public void DrawSprite(OpenRASprite s, Vector2 location, Size size)
	{
		DrawSprite(s, location, 0, size);
	}
	
	public void Flush() {

	}
}
