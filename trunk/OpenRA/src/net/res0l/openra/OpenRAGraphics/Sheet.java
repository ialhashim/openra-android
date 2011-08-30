package net.res0l.openra.OpenRAGraphics;

import net.res0l.openra.*;
import net.res0l.openra.OpenRAFileFormat.IGraphicsDevice.ITexture;

public class Sheet {
	
	Bitmap bitmap;
	ITexture texture;
	boolean dirty;
	byte[] data;
	public Size Size;

	public Sheet(Size size)
	{
		Size = size;
	}

	public Sheet(String filename)
	{
		bitmap = new Bitmap(filename);
		Size = bitmap.Size;
	}

	public ITexture Texture()
	{
		if (texture == null)
		{
			texture = Game.Renderer.Device.CreateTexture();
			dirty = true;
		}

		if (dirty)
		{
			if (data != null)
			{
				texture.SetData(data, Size.Width, Size.Height);
				dirty = false;
			}
			else if (bitmap != null)
			{
				texture.SetData(bitmap);
				dirty = false;
			}
		}

		return texture;
	}

	public byte[] Data() { if (data == null) data = new byte[4 * Size.Width * Size.Height]; return data; }
	public void MakeDirty() { dirty = true; }
}
