package net.res0l.openra.OpenRAGameRules;

import net.res0l.openra.Support.Arguments;
import net.res0l.openra.Server.*;

public class Settings {

	public Settings(String string, Arguments args) {
		// TODO Auto-generated constructor stub
	}

	public GameSettings Game;
	public GraphicSettings Graphics;
	public Server Server;
	
	public class GameSettings
	{
		// Internal game settings
		public int Timestep = 40;
		public int SheetSize = 2048;
		public String[] Mods;
	}
	
	public class GraphicSettings
	{
		
	}

	public void Save() {
		// TODO Auto-generated method stub
		
	}
}
