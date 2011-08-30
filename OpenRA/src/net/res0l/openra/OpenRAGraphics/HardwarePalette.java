package net.res0l.openra.OpenRAGraphics;

import java.util.HashMap;
import java.util.Map;

import net.res0l.openra.Game;
import net.res0l.openra.OpenRAFileFormat.*;
import net.res0l.openra.OpenRAFileFormat.IGraphicsDevice.ITexture;
import net.res0l.openra.OpenRAGame.TraitsInterfaces.*;

public class HardwarePalette {
	public int MaxPalettes = 256;
	int allocated = 0;
	
	ITexture texture;
	Map<String, Palette> palettes;
	Map<String, Integer> indices;
	
	public HardwarePalette()
	{
		palettes = new HashMap<String, Palette>();
		indices = new HashMap<String, Integer>();
		texture = Game.Renderer.Device.CreateTexture();
	}
	
	public Palette GetPalette(String name)
	{
		return palettes.get(name);
	}

	public int GetPaletteIndex(String name)
	{
		return indices.get(name);
	}
	
	public void AddPalette(String name, Palette p)
	{
		palettes.put(name, p);
		indices.put(name, allocated++);
	}

	long[][] data = new long[MaxPalettes][256];
	
	public void Update(IPaletteModifier[] iPaletteModifiers)
	{
		/*var copy = palettes.ToMap(p => p.Key, p => new Palette(p.Value));
		
		for (var mod : iPaletteModifiers)
			mod.AdjustPalette(copy);
		
		for (var pal : copy)
		{
			int j = indices[pal.Key];
			byte c = pal.Value.Values;
			
			for (var i = 0; i < 256; i++)
				data[j,i] = c[i];
		}*/
        
		// Doesn't work
		texture.SetData(data);
		Game.Renderer.PaletteTexture = texture;
	}
}

