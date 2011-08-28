package net.res0l.openra.OpenRANetwork;

import com.badlogic.gdx.utils.Disposable;

import net.res0l.openra.World;
import net.res0l.openra.OpenRANetwork.ReplayConnection.ReplayRecorderConnection;

public class OrderManager implements Disposable {

	public int NetFrameNumber;
	public int LocalFrameNumber;
	public Connection Connection;

	public OrderManager(String host, int port, IConnection connection) {
	}
	
	public World world;
	public long LastTickTime;
	public boolean GameStarted;
	public boolean IsReadyForNextFrame;

	public void Tick() {
	}

	public void StartGame() {	
	}

	public void dispose() {

	}

}
