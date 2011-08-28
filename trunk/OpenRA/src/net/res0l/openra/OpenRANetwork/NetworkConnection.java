package net.res0l.openra.OpenRANetwork;

import net.res0l.openra.OpenRANetwork.Connection.ConnectionState;

public class NetworkConnection implements IConnection
{
	int clientId;
	ConnectionState connectionState = ConnectionState.Connecting;
	
	public NetworkConnection(String host, int port) {
		// TODO Auto-generated constructor stub
	}
}