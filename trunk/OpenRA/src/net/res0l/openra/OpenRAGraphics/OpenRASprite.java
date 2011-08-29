package net.res0l.openra.OpenRAGraphics;

import net.res0l.openra.Game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class OpenRASprite extends Sprite
{
	public Rectangle bounds;
	public Sheet sheet;
	public TextureChannel channel;
	public Rectangle uv;
	public Vector2 size;

	Vector2[] uvhax;

	public OpenRASprite(Sheet sheet, Rectangle bounds, TextureChannel channel)
	{
		this.bounds = bounds;
		this.sheet = sheet;
		this.channel = channel;

		uv = new Rectangle(
				(float)(bounds.x) / sheet.Size.x,
				(float)(bounds.y) / sheet.Size.y,
				(float)(bounds.width) / sheet.Size.x,
				(float)(bounds.height) / sheet.Size.y);

		uvhax = new Vector2[]
		{
			new Vector2( uv.x, uv.y ),
			new Vector2( uv.width, uv.y ),
			new Vector2( uv.x, uv.height ),
			new Vector2( uv.width, uv.height ),
		};

		this.size = new Vector2(bounds.x, bounds.y);
	}

	public Vector2 FastMapTextureCoords( int k )
	{
		return uvhax[ k ];
	}

	public void DrawAt( WorldRenderer wr, Vector2 location, String palette )
	{
		Game.Renderer.SpriteRenderer.DrawSprite( this, location, wr, palette, this.size );
	}

	public void DrawAt( Vector2 location, int paletteIndex )
	{
		Game.Renderer.SpriteRenderer.DrawSprite( this, location, paletteIndex, this.size );
	}

    public void DrawAt(Vector2 location, int paletteIndex, float scale)
    {
        Game.Renderer.SpriteRenderer.DrawSprite(this, location, paletteIndex, this.size.mul(scale));
    }

	public void DrawAt( Vector2 location, int paletteIndex, Vector2 size )
	{
		Game.Renderer.SpriteRenderer.DrawSprite( this, location, paletteIndex, size );
	}
	
	public enum TextureChannel{
		Red,
		Green,
		Blue,
		Alpha,
	}
}
