package net.res0l.openra.OpenRAFileFormat;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import org.yaml.snakeyaml.*;

public class Mod
{
	public String Title;
	public String Description;
	public String Version;
	public String Author;
	public String Requires;
	public boolean Standalone = false;

	public static Map<String, Mod> AllMods = ValidateMods(new FileHandle("mods").list());

	public static Map<String, Mod> ValidateMods(FileHandle[] fileHandles)
	{
		Map<String, Mod> ret = new HashMap<String, Mod>();
		
		Array<String> folderNames = new Array<String>();
		
		for(FileHandle f : fileHandles)
			folderNames.add(f.name());
		
		for (String m : folderNames)
		{
			String yamlFilename = "mods/" + m + "/mod.yaml";
			
			FileHandle ymalModFile = new FileHandle(yamlFilename);
			
			if (!ymalModFile.exists())
				continue;

			Yaml yaml = new Yaml();
			yaml.load(yamlFilename);
			
			//ret.Add(m, FieldLoader.Load<Mod>(yaml.NodesDict["Metadata"]));
			ret.put(m, new Mod());
		}
		
		return ret;
	}
}
