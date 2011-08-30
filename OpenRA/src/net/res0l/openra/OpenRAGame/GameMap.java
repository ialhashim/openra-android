package net.res0l.openra.OpenRAGame;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.res0l.openra.OpenRAFileFormat.FileSystem;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameMap {
	protected IFolder Container;
	public String Path;
	
	// Yaml map data
	public String Uid;
	public int MapFormat;
	public boolean Selectable;
    public boolean UseAsShellmap;
	public String RequiresMod;

	public String Title;
	public String Type = "Conquest";
	public String Description;
	public String Author;
	public String Tileset;
	
	public Map<String, ActorReference> Actors;

	public int PlayerCount() { return SpawnPoints().size() }
	
	public Array<Vector2> SpawnPoints()
	{
		return Actors.Value.Values.Where(a => a.Type == "mpspawn").Select(a => a.InitDict.Get<LocationInit>().value);
	}
	
	public Rectangle Bounds;
			
	
	// Yaml map data
	public Map<String, PlayerReference> Players = new HashMap<String, PlayerReference>();
	public List<SmudgeReference> Smudges;

	// Rules overrides
	public List<Node> Rules = new List<MiniYamlNode>();

	// Sequences overrides
	public List<MiniYamlNode> Sequences = new List<MiniYamlNode>();

	// Weapon overrides
	public List<MiniYamlNode> Weapons = new List<MiniYamlNode>();

	// Voices overrides
	public List<MiniYamlNode> Voices = new List<MiniYamlNode>();

	// Binary map data
	public byte TileFormat = 1;
	public Vector2 MapSize;

	public TileReference<Short, Byte>[][] MapTiles;
	public TileReference<Byte, Byte>[][] MapResources;
	public String [][] CustomTerrain;

	public GameMap()
	{
		// Do nothing; not a valid map (editor hack)
	}
	
	public static GameMap FromTileset(String tileset)
	{
		var tile = OpenRA.Rules.TileSets[tileset].Templates.First();
		GameMap map = new GameMap();
		
		map.Title = "Name your map here";
		map.Description = "Describe your map here";
		map.Author = "Your name here";
		map.MapSize = new Vector2(1, 1);
		map.Tileset = tileset;
		map.MapResources = Lazy.New(() => new TileReference<byte, byte>[1, 1]);
		map.MapTiles = Lazy.New(() => new TileReference<ushort, byte>[1, 1]
		{ { new TileReference<ushort, byte> { 
			type = tile.Key, 
			index = (byte)0 }
		} });
		map.Actors = Lazy.New(() => new Map<String, ActorReference>());
		map.Smudges = Lazy.New(() => new List<SmudgeReference>());
	
		return map;
	}

	class Format2ActorReference
	{
		public String Id = null;
		public String Type = null;
		public Vector2 Location = Vector2.Zero;
		public String Owner = null;
	}
	
	public GameMap(String path)
	{
		Path = path;
		Container = FileSystem.OpenPackage(path, Integer.MAX_VALUE);
		var yaml = new MiniYaml( null, MiniYaml.FromStream(Container.GetContent("map.yaml")) );
		FieldLoader.Load(this, yaml);
        Uid = ComputeHash();
					
		// 'Simple' metadata
		FieldLoader.Load( this, yaml );

		// Load players
		for (var kv : yaml.NodesDict["Players"].NodesDict)
		{
			var player = new PlayerReference(kv.Value);
			Players.Add(player.Name, player);
		}
		
		Actors = Lazy.New(() =>
		{
			var ret =  new Map<String, ActorReference>();
			// Load actors
			for (var kv : yaml.NodesDict["Actors"].NodesDict)
				ret.Add(kv.Key, new ActorReference(kv.Value.Value, kv.Value.NodesDict));
			
			// Add waypoint actors

			if (MapFormat < 5)
				for( var wp : yaml.NodesDict[ "Waypoints" ].NodesDict )
				{
					String[] loc = wp.Value.Value.Split( ',' );
					var a = new ActorReference("mpspawn");
					a.Add(new LocationInit(new Vector2( int.Parse( loc[ 0 ] ), int.Parse( loc[ 1 ] ) )));
					a.Add(new OwnerInit(Players.First(p => p.Value.OwnsWorld).Key));
					ret.Add(wp.Key, a);
				}
			
			return ret;
		});
		

		// Upgrade map to format 5
		if (MapFormat < 5)
		{
			// Define RequiresMod for map installer
			RequiresMod = Game.CurrentMods.Keys.First();
												
			var TopLeft = (Vector2)FieldLoader.GetValue( "", typeof(Vector2), yaml.NodesDict["TopLeft"].Value);
			var BottomRight = (Vector2)FieldLoader.GetValue( "", typeof(Vector2), yaml.NodesDict["BottomRight"].Value);
			Bounds = Rectangle.FromLTRB(TopLeft.X, TopLeft.Y, BottomRight.X, BottomRight.Y);		
			
			// Creep player
			for (var mp : Players.Where(p => !p.Value.NonCombatant && !p.Value.Enemies.Contains("Creeps")))
				mp.Value.Enemies = mp.Value.Enemies.Concat(new[] {"Creeps"}).ToArray();
			
			Players.Add("Creeps", new PlayerReference
			{
				Name = "Creeps",
				Race = "Random",
				NonCombatant = true,
				Enemies = Players.Keys.Where(k => k != "Neutral").ToArray()
			});
		}
		
		/* hack: make some slots. */
		if (!Players.Any(p => p.Value.Playable))
		{
			for (int index = 0; index < SpawnPoints.Count(); index++)
			{
				var p = new PlayerReference
				{
					Name = "Multi{0}".F(index),
					Race = "Random",
					Playable = true,
					DefaultStartingUnits = true,
					Enemies = new[]{"Creeps"}
				};
				Players.Add(p.Name, p);
			}
		}
					
		// Smudges
		Smudges = Lazy.New(() =>
		{
			var ret = new List<SmudgeReference>();
			for (var kv : yaml.NodesDict["Smudges"].NodesDict)
			{
				String[] vals = kv.Key.Split(' ');
				String[] loc = vals[1].Split(',');
				ret.Add(new SmudgeReference(vals[0], new Vector2(int.Parse(loc[0]), int.Parse(loc[1])), int.Parse(vals[2])));
			}
			
			return ret;
		});
		

		// Rules
		Rules = yaml.NodesDict["Rules"].Nodes;

		// Sequences
		Sequences = (yaml.NodesDict.containsKey("Sequences")) ? yaml.NodesDict["Sequences"].Nodes : new List<MiniYamlNode>();

		// Weapons
		Weapons = (yaml.NodesDict.containsKey("Weapons")) ? yaml.NodesDict["Weapons"].Nodes : new List<MiniYamlNode>();
		
		// Voices
		Voices = (yaml.NodesDict.containsKey("Voices")) ? yaml.NodesDict["Voices"].Nodes : new List<MiniYamlNode>();

		CustomTerrain = new String[MapSize.X, MapSize.Y];
		
		MapTiles = Lazy.New(() => LoadMapTiles());
		MapResources = Lazy.New(() => LoadResourceTiles());
	}

	public void Save(String toPath)
	{			
		MapFormat = 5;
		
		var root = new List<MiniYamlNode>();
		var fields = new String[]
		{
			"Selectable",
			"MapFormat",
			"RequiresMod",
			"Title",
			"Description",
			"Author",
			"Tileset",
			"MapSize",
			"Bounds",
			"UseAsShellmap",
			"Type",
		};
		
		for (var field : fields)
		{
			var f = this.GetType().GetField(field);
			if (f.GetValue(this) == null) continue;
			root.Add( new MiniYamlNode( field, FieldSaver.FormatValue( this, f ) ) );
		}

		root.Add( new MiniYamlNode( "Players", null,
			Players.Select( p => new MiniYamlNode(
				"PlayerReference@{0}".F( p.Key ),
				FieldSaver.SaveDifferences( p.Value, new PlayerReference() ) ) ).ToList() ) );

		root.Add( new MiniYamlNode( "Actors", null,
			Actors.Value.Select( x => new MiniYamlNode(
				x.Key,
				x.Value.Save() ) ).ToList() ) );

		root.Add(new MiniYamlNode("Smudges", MiniYaml.FromList<SmudgeReference>( Smudges.Value )));
		root.Add(new MiniYamlNode("Rules", null, Rules));
		root.Add(new MiniYamlNode("Sequences", null, Sequences));
		root.Add(new MiniYamlNode("Weapons", null, Weapons));
		root.Add(new MiniYamlNode("Voices", null, Voices));
		
		Map<String, byte[]> entries = new HashMap<String, byte[]>();
		entries.Add("map.bin", SaveBinaryData());
		var s = root.WriteToString();
		entries.Add("map.yaml", Encoding.UTF8.GetBytes(s));
		
		// Saving the map to a new location
		if (toPath != Path)
		{
			Path = toPath;
			
			// Create a new map package
			// TODO: Add other files (resources, rules) to the entries list
			Container = FileSystem.CreatePackage(Path, int.MaxValue, entries);
		}
		
		// Update existing package
		Container.Write(entries);
	}

	static byte ReadByte(InputStream s)
	{
		int ret = s.read();
		return (byte)ret;
	}

	static short ReadWord(InputStream s)
	{
		short ret = ReadByte(s);
		ret |= (short)(ReadByte(s) << 8);

		return ret;
	}
	
	public TileReference<Short, Byte>[][] LoadMapTiles()
	{
		var tiles = new TileReference<ushort, Byte>[MapSize.X, MapSize.Y];
		using (var dataStream = Container.GetContent("map.bin"))
		{
			if (ReadByte(dataStream) != 1)
				throw new InvalidDataException("Unknown binary map format");

			// Load header info
			var width = ReadWord(dataStream);
			var height = ReadWord(dataStream);

			if (width != MapSize.X || height != MapSize.Y)
				throw new InvalidDataException("Invalid tile data");


			// Load tile data
			for (int i = 0; i < MapSize.X; i++)
				for (int j = 0; j < MapSize.Y; j++)
				{
					ushort tile = ReadWord(dataStream);
					byte index = ReadByte(dataStream);
					if (index == byte.MaxValue)
						index = (byte)(i % 4 + (j % 4) * 4);

					tiles[i, j] = new TileReference<ushort, byte>(tile, index);
				}
		}
		return tiles;
	}
	
	public TileReference<Byte, Byte>[][] LoadResourceTiles()
	{
		var resources = new TileReference<Byte, Byte>[MapSize.X, MapSize.Y];

		using (var dataStream = Container.GetContent("map.bin"))
		{
			if (ReadByte(dataStream) != 1)
				throw new InvalidDataException("Unknown binary map format");

			// Load header info
			var width = ReadWord(dataStream);
			var height = ReadWord(dataStream);

			if (width != MapSize.X || height != MapSize.Y)
				throw new InvalidDataException("Invalid tile data");
			
			// Skip past tile data
			for (var i = 0; i < 3*MapSize.X*MapSize.Y; i++)
				ReadByte(dataStream);

			// Load resource data
			for (int i = 0; i < MapSize.X; i++)
				for (int j = 0; j < MapSize.Y; j++)
			{
				byte type = ReadByte(dataStream);
				byte index = ReadByte(dataStream);
				resources[i, j] = new TileReference<byte, byte>(type, index);
			}
		}
		return resources;
	}

	public byte[] SaveBinaryData()
	{
		MemoryStream dataStream = new MemoryStream();
		
		var writer = new BinaryWriter(dataStream));
	
		// File header consists of a version byte, followed by 2 ushorts for width and height
		writer.Write(TileFormat);
		writer.Write((ushort)MapSize.X);
		writer.Write((ushort)MapSize.Y);

		if (!OpenRA.Rules.TileSets.containsKey(Tileset))
			throw new InvalidOperationException(
				"Tileset used by the map ({0}) does not exist in this mod. Valid tilesets are: {1}"
				.F(Tileset, String.Join(",", OpenRA.Rules.TileSets.Keys.ToArray())));

		// Tile data
		for (int i = 0; i < MapSize.X; i++)
		{
			for (int j = 0; j < MapSize.Y; j++)
			{
				writer.Write(MapTiles.Value[i, j].type);
				var PickAny = OpenRA.Rules.TileSets[Tileset].Templates[MapTiles.Value[i, j].type].PickAny;
				writer.Write(PickAny ? (byte)(i % 4 + (j % 4) * 4) : MapTiles.Value[i, j].index);
			}
		}

		// Resource data	
		for (int i = 0; i < MapSize.X; i++)
		{
			for (int j = 0; j < MapSize.Y; j++)
			{
				writer.Write(MapResources.Value[i, j].type);
				writer.Write(MapResources.Value[i, j].index);
			}
		}

		return dataStream.ToArray();
	}

	public boolean IsInMap(Vector2 xy)
	{
		return IsInMap((int)xy.x, (int)xy.y);
	}

	public boolean IsInMap(int x, int y)
	{
		return Bounds.contains(x,y);
	}

	static T[][] ResizeArray<T>(T[][] ts, T t, int width, int height)
	{
		var result = new T[width, height];
		for (var i = 0; i < width; i++)
		{
			for (var j = 0; j < height; j++)
			{
				result[i, j] = i <= ts.GetUpperBound(0) && j <= ts.GetUpperBound(1) ? ts[i, j] : t;
			}
		}
		return result;
	}

	public void Resize(int width, int height)		// editor magic.
	{
		var oldMapTiles = MapTiles.Value;
		var oldMapResources = MapResources.Value;

		MapTiles = Lazy.New(() => ResizeArray(oldMapTiles, oldMapTiles[0, 0], width, height));
		MapResources = Lazy.New(() => ResizeArray(oldMapResources, oldMapResources[0, 0], width, height));
		MapSize = new Vector2(width, height);
	}
	
	public void ResizeCordon(int left, int top, int right, int bottom)
	{
		Bounds = new Rectangle(left, top, right, bottom);
	}
	
	String ComputeHash()
    {
        // UID is calculated by taking an SHA1 of the yaml and binary data
        // Read the relevant data into a buffer
        var data = Container.GetContent("map.yaml").ReadAllBytes()
            .Concat(Container.GetContent("map.bin").ReadAllBytes()).ToArray();

        // Take the SHA1
        var csp = SHA1.Create();
        return new String(csp.ComputeHash(data).SelectMany(a => a.ToString("x2")).ToArray());
    }
}
