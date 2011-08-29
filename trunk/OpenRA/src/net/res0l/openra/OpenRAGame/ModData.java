package net.res0l.openra.OpenRAGame;

import java.util.Map;

import net.res0l.openra.OpenRAFileFormat.*;
import net.res0l.openra.OpenRAGame.GameMap;
import net.res0l.openra.OpenRAWidget.*;

public class ModData {
	public Manifest Manifest = new Manifest();
	public WidgetLoader WidgetLoader = new WidgetLoader();
	public Map<String, GameMap> AvailableMaps;

	public ModData(String[] mm) {
		
	}

	public GameMap PrepareMap(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void LoadInitialAssets() {
		// TODO Auto-generated method stub
		
	}
}
