package net.res0l.openra.OpenRAGraphics;

import java.util.*;

import net.res0l.openra.OpenRAGame.ModData;

public class CursorSheetBuilder {
	ModData modData;
	Map<String, OpenRASprite[]> cursors;
	String[] exts = { ".shp" };

	public CursorSheetBuilder( ModData modData )
	{
		this.modData = modData;
		this.cursors = new HashMap<String, OpenRASprite[]>();
	}

	OpenRASprite[] LoadCursors(String filename)
    {
		/*try
		{
			var shp = new Dune2ShpReader(FileSystem.OpenWithExts(filename, exts));
			return shp.Select(a => modData.SheetBuilder.Add(a.Image, a.Size)).ToArray();
		}
		catch (IndexOutOfRangeException) // This will occur when loading a custom (RA-format) .shp
		{
			var shp = new ShpReader(FileSystem.OpenWithExts(filename, exts));
			return shp.Select(a => modData.SheetBuilder.Add(a.Image, shp.Size)).ToArray();
		}*/
		
		return null;
	}

	public OpenRASprite[] LoadAllSprites(String filename) { 
		return cursors.get(filename); 
	}
}
