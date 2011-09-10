package net.res0l.openra.OpenRAFileFormat;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

public class Manifest
{
	public String[]
		Mods, Folders, Packages, Rules, ServerTraits,
		Sequences, Cursors, Chrome, Assemblies, ChromeLayout,
		Weapons, Voices, Music, Movies, TileSets;
	public String LoadScreen;
	public int TileSize = 24;

	public Manifest(String[] mods)
	{
		Mods = mods;

		//var yaml = mods.Select(m => MiniYaml.FromFile("mods/" + m + "/mod.yaml")).Aggregate(MiniYaml.MergeLiberal);
		
		Yaml yaml = new Yaml();
		yaml.load("mods/" + mods[0] + "/mod.yaml");
		
	
		// Todo: Use fieldloader
	
		Folders = YamlList(yaml, "Folders");
		Packages = YamlList(yaml, "Packages");
		Rules = YamlList(yaml, "Rules");
		ServerTraits = YamlList(yaml, "ServerTraits");
		Sequences = YamlList(yaml, "Sequences");
		Cursors = YamlList(yaml, "Cursors");
		Chrome = YamlList(yaml, "Chrome");
		Assemblies = YamlList(yaml, "Assemblies");
		ChromeLayout = YamlList(yaml, "ChromeLayout");
		Weapons = YamlList(yaml, "Weapons");
		Voices = YamlList(yaml, "Voices");
		Music = YamlList(yaml, "Music");
		Movies = YamlList(yaml, "Movies");
		TileSets = YamlList(yaml, "TileSets");

		LoadScreen = yaml.First( x => x.Key == "LoadScreen" ).Value.Value;
		
		TileSize = Integer.Parse(yaml.First("TileSize").Value.Value);
	}

	static String[] YamlList(ArrayList<Node> ys, String key)
	{
		Object y = ys.get(key);
		
		if( y == null )
			return new String[ 0 ];

		return y.Value.NodesDict.Keys.ToArray();
	}
}