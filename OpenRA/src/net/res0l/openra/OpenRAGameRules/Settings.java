package net.res0l.openra.OpenRAGameRules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import net.res0l.openra.Support.Arguments;
import net.res0l.openra.InputHandler.DefaultInputHandler.Modifiers;
import net.res0l.openra.OpenRAFileFormat.Graphics.WindowMode;
import net.res0l.openra.OpenRAFileFormat.ColorRamp;

public class Settings {

	public class ServerSettings
	{
		public String Name = "OpenRA Game";
		public int ListenPort = 1234;
		public int ExternalPort = 1234;
		public boolean AdvertiseOnline = true;
		public String MasterServer = "http://master.open-ra.org/";
		public boolean AllowCheats = false;
	}
	
	public class DebugSettings
	{
		public boolean BotDebug = false;
		public boolean PerfGraph = false;
		public float LongTickThreshold = 0.001f;
        public boolean SanityCheckUnsyncedCode = false;
	}

	public class GraphicSettings
	{
		public String Renderer = "Gl";
		public WindowMode Mode = WindowMode.Windowed;
		public Vector2 FullscreenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		public Vector2 WindowedSize = new Vector2(800, 600);
		public Vector2 MinResolution = new Vector2(800, 600);
	}
	
	public class SoundSettings
	{
		public float SoundVolume = 0.5f;
		public float MusicVolume = 0.5f;
		public float VideoVolume = 0.5f;
		public boolean Shuffle = false;
		public boolean Repeat = false;
	}
	
	public class PlayerSettings
	{
		public String Name = "Newbie";
        public ColorRamp ColorRamp = new ColorRamp(75, 255, 180, 25);
		public String LastServer = "localhost:1234";
	}
	
	public class GameSettings
	{
		public String[] Mods = { "ra" };
			
		public boolean MatchTimer = true;
		
		// Chat settings
		public boolean TeamChatToggle = false;

		// Behavior settings
        public boolean ViewportEdgeScroll = true;
        public boolean InverseDragScroll = false;
		public float ViewportEdgeScrollStep = 10f;

		// Internal game settings
		public int Timestep = 40;
		public int SheetSize = 2048;
	}
	
	public class KeyboardSettings
	{
		public Modifiers ControlGroupModifier = Modifiers.Ctrl;
	}

	String SettingsFile;
	
	public PlayerSettings Player = new PlayerSettings();
	public GameSettings Game = new GameSettings();
	public SoundSettings Sound = new SoundSettings();
	public GraphicSettings Graphics = new GraphicSettings();
	public ServerSettings Server = new ServerSettings();
	public DebugSettings Debug = new DebugSettings();
	public KeyboardSettings Keyboard = new KeyboardSettings();
	public Map<String, Object> Sections;
	
	public Settings(String file, Arguments args)
	{			
		SettingsFile = file;
		Sections = new HashMap<String, Object>();
		
		Sections.put("Player", Player);
		Sections.put("Game", Game);
		Sections.put("Sound", Sound);
		Sections.put("Player", Player);
		Sections.put("Player", Player);
		Sections.put("Graphics", Graphics);
		Sections.put("Server", Server);
		Sections.put("Debug", Debug);
		Sections.put("Keyboard", Keyboard);
		
		//if (File.Exists(SettingsFile))
		//{
			//Console.WriteLine("Loading settings file {0}",SettingsFile);
			//var yaml = MiniYaml.DictFromFile(SettingsFile);
			
			//foreach (var kv in Sections)
			//	if (yaml.ContainsKey(kv.Key))
			//		LoadSectionYaml(yaml[kv.Key], kv.Value);
		//}
		
		// Override with commandline args
		//foreach (var kv in Sections)
		//	foreach (var f in kv.Value.GetType().GetFields())
		//		if (args.Contains(kv.Key+"."+f.Name))
		//			FieldLoader.LoadField( kv.Value, f.Name, args.GetValue(kv.Key+"."+f.Name, "") );
		
		//FieldLoader.UnknownFieldAction = err1;
		//FieldLoader.InvalidValueAction = err2;
	}
	
	public void Save()
	{
		//var root = new List<MiniYamlNode>();
		//foreach( var kv in Sections )
		//	root.Add( new MiniYamlNode( kv.Key, FieldSaver.SaveDifferences(kv.Value, Activator.CreateInstance(kv.Value.GetType())) ) );
		
		//root.WriteToFile(SettingsFile);
	}
	
	/*void LoadSectionYaml(MiniYaml yaml, object section)
	{
		object defaults = Activator.CreateInstance(section.GetType());
		FieldLoader.InvalidValueAction = (s,t,f) =>
		{
			object ret = defaults.GetType().GetField(f).GetValue(defaults);
			System.Console.WriteLine("FieldLoader: Cannot parse `{0}` into `{2}:{1}`; substituting default `{3}`".F(s,t.Name,f,ret) );
			return ret;
		};
		
		FieldLoader.Load(section, yaml);
	}*/
}
