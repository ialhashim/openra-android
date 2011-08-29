package net.res0l.openra.OpenRAGame;

import java.util.HashMap;
import java.util.Map;

import net.res0l.openra.Console;
import net.res0l.openra.OpenRAFileFormat.*;
import net.res0l.openra.OpenRAGame.GameMap;
import net.res0l.openra.OpenRAWidget.*;
import net.res0l.openra.OpenRAGraphics.*;

public class ModData 
{
	public Manifest Manifest;
	public ObjectCreator ObjectCreator;
	public Map<String, GameMap> AvailableMaps;
	public WidgetLoader WidgetLoader;
	public SheetBuilder SheetBuilder;
	public CursorSheetBuilder CursorSheetBuilder;
	public SpriteLoader SpriteLoader;
	public HardwarePalette Palette;
	
	IFolder previousMapMount = null;

	public interface ILoadScreen { void Display(); void Init(); }
	public ILoadScreen LoadScreen = null;
	
	public ModData( String[] mods )
	{		
		Manifest = new Manifest( mods );
		ObjectCreator = new ObjectCreator( Manifest );
		LoadScreen = ObjectCreator.CreateObject<ILoadScreen>(Manifest.LoadScreen);
		LoadScreen.Init();
		LoadScreen.Display();
		WidgetLoader = new WidgetLoader( this );
	}
	
	public void ReloadMaps()
	{
		AvailableMaps = FindMaps( Manifest.Mods );
	}
		
	public void LoadInitialAssets()
	{
		for (String dir : Manifest.Folders)
			FileSystem.Mount(dir);
		
		ReloadMaps();
		Palette = new HardwarePalette();
		//ChromeProvider.Initialize( Manifest.Chrome );
		SheetBuilder = new SheetBuilder( TextureChannel.Red );
		CursorSheetBuilder = new CursorSheetBuilder( this );
		CursorProvider.Initialize(Manifest.Cursors);
		Palette.Update(new IPaletteModifier[]{});
	}

	public GameMap PrepareMap(String uid)
	{
		LoadScreen.Display();
		if (!AvailableMaps.ContainsKey(uid))
			throw new Exception("Invalid map uid: " + uid);
		
		var GameMap = new GameMap(AvailableMaps[uid].Path);

		// Maps may contain custom assets
		// TODO: why are they lowest priority? they should be highest.
		if (previousMapMount != null) FileSystem.Unmount(previousMapMount);
		previousMapMount = FileSystem.OpenPackage(map.Path, int.MaxValue);
		FileSystem.Mount(previousMapMount);
		
		// Reinit all our assets
		LoadInitialAssets();
		for (var pkg : Manifest.Packages)
			FileSystem.Mount(pkg);
	
		Rules.LoadRules(Manifest, map);
		SpriteLoader = new SpriteLoader( Rules.TileSets[map.Tileset].Extensions, SheetBuilder );
		SequenceProvider.Initialize(Manifest.Sequences, map.Sequences);
		
		return map;
	}
	
    public static IEnumerable<String> FindMapsIn(String dir)
    {
        String[] NoMaps = { };

        if (!Directory.Exists(dir))
            return NoMaps;

        return Directory.GetDirectories(dir)
            .Concat(Directory.GetFiles(dir, "*.zip"))
            .Concat(Directory.GetFiles(dir, "*.oramap"));
    }

	Map<String, GameMap> FindMaps(String[] mods)
	{
        String[] paths = mods.SelectMany(p => FindMapsIn("mods{0}{1}{0}maps{0}".F(Path.DirectorySeparatorChar, p)))
			.Concat(mods.SelectMany(p => FindMapsIn("{1}maps{0}{2}{0}".F(Path.DirectorySeparatorChar, Game.SupportDir, p))));
		
		Map<String, GameMap> ret = new HashMap<String, GameMap>();
		
		for (String path : paths)
		{
			GameMap map = new GameMap(path);
			
			if (ret.ContainsKey(map.Uid))
				Console.WriteLine("Ignoring duplicate map:" + path);
			else
				ret.Add(map.Uid, map);
		}
		return ret;
	}
	
}
