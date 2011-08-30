package net.res0l.openra.OpenRAGraphics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import net.res0l.openra.Size;
import net.res0l.openra.Util;
import net.res0l.openra.OpenRAGraphics.OpenRASprite.TextureChannel;

public class SheetBuilder {
	
	Sheet current = null;
	int rowHeight = 0;
	Vector2 p;
	TextureChannel channel = null;
	TextureChannel initialChannel;
	
	public SheetBuilder(TextureChannel ch)
	{
		current = null;
		rowHeight = 0;
		channel = null;
		initialChannel = ch;
	}

	public OpenRASprite Add(byte[] src, Size size)
	{
		OpenRASprite rect = Allocate(size);
		Util.FastCopyIntoChannel(rect, src);
		return rect;
	}

	public OpenRASprite Add(Size size, byte paletteIndex)
	{
		byte[] data = new byte[(int) (size.Width * size.Height)];
		
		for (int i = 0; i < data.length; i++)
			data[i] = paletteIndex;

		return Add(data, size);
	}

	Sheet NewSheet() { return new Sheet(new Size( Renderer.SheetSize, Renderer.SheetSize ) ); }


	TextureChannel NextChannel(TextureChannel t)
	{
		if (t == null)
			return initialChannel;

		switch (t)
		{
			case Red: return TextureChannel.Green;
			case Green: return TextureChannel.Blue;
			case Blue: return TextureChannel.Alpha;
			case Alpha: return null;

			default: return null;
		}
	}

	public OpenRASprite Allocate(Size imageSize)
	{
		if (current == null)
		{
			current = NewSheet();
			channel = NextChannel(null);
		}

		if (imageSize.Width + p.x > current.Size.Width)
		{
			p = new Vector2(0, p.y + rowHeight);
			rowHeight = imageSize.Height;
		}

		if (imageSize.Height > rowHeight)
			rowHeight = imageSize.Height;

		if (p.y + imageSize.Height > current.Size.Height)
		{

			if (null == (channel = NextChannel(channel)))
			{
				current = NewSheet();
				channel = NextChannel(channel);
			}

			rowHeight = imageSize.Height;
			p = new Vector2(0,0);
		}

		OpenRASprite rect = new OpenRASprite(current, new Rectangle(p.x, p.y, imageSize.Width, imageSize.Height), channel);
		current.MakeDirty();
		p.x += imageSize.Width;

		return rect;
	}
}
