package net.res0l.openra;

import java.util.HashMap;
import java.util.Map;

import net.res0l.openra.OpenRAFileFormat.*;
import net.res0l.openra.OpenRAGame.GameMap;
import net.res0l.openra.OpenRAWidget.*;

public class ModData {
	public Manifest Manifest = new Manifest();
	public WidgetLoader WidgetLoader = new WidgetLoader();
	public Map<String, GameMap> AvailableMaps = new HashMap<String, GameMap>();

	public ModData(String[] mm) {
		// TODO Auto-generated constructor stub
	}

	public GameMap PrepareMap(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void LoadInitialAssets() {
		// TODO Auto-generated method stub
		
	}
}
