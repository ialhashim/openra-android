package net.res0l.openra.OpenRAFileFormat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.graphics.Color;

public class Palette {
	long[] colors;
	
	public Color GetColor(int index)
	{
		return new Color(colors[index],colors[index],colors[index],colors[index]);
	}
	
	public void SetColor(int index, Color color)
	{
		colors[index] = (long)color.toIntBits();
	}
	
	public void SetColor(int index, long color)
	{
		colors[index] = (long)color;
	}
			
	public long[] Values()
	{			
		return colors;
	}

	public Palette(String filename, boolean remapTransparent) throws IOException
	{
		colors = new long[256];

		FileInputStream reader;
		
		try {
			reader = new FileInputStream(filename);

			for (int i = 0; i < 256; i++)
			{
				byte r = (byte)(reader.read() << 2);
				byte g = (byte)(reader.read() << 2);
				byte b = (byte)(reader.read() << 2);
				colors[i] = (long)((255 << 24) | (r << 16) | (g << 8) | b);
			}
		
			colors[0] = 0;
			if (remapTransparent)
			{
				colors[1] = 178 << 24; // Hack for d2k; may have side effects
				colors[3] = 178 << 24;
				colors[4] = 140 << 24;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Palette(Palette p, IPaletteRemap r)
	{
		colors = new long[256];
		for(int i = 0; i < 256; i++)
		{
			colors[i] = (long)r.GetRemappedColor(new Color((int)p.colors[i], 0, 0, 0),i).toIntBits();
		}
	}
	
	public Palette(Palette p)
	{
		colors = (long[])p.colors.clone();
	}
	
	public interface IPaletteRemap { Color GetRemappedColor(Color original, int index);	}
}
