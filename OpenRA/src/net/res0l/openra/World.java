package net.res0l.openra;

import net.res0l.openra.OpenRANetwork.OrderManager;
import net.res0l.openra.Orders.IOrderGenerator;
import net.res0l.openra.OpenRAFileFormat.Manifest;
import net.res0l.openra.OpenRAGame.*;

public class World {

	public final TraitDictionary traitDict;
	public OrderManager orderManager;
	public Player LocalPlayer;
	public IOrderGenerator OrderGenerator;
	public Selection Selection;
	public World(Manifest manifest, GameMap map, OrderManager orderManager2) {
		// TODO Auto-generated constructor stub
	}
	public void Tick() {
		// TODO Auto-generated method stub
		
	}
}
