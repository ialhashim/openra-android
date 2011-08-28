package net.res0l.openra.OpenRANetwork;

public class Connection {
    public enum ConnectionState
	{
		PreConnecting,
		NotConnected,
		Connecting,
		Connected,
	}

	public int LocalClientId;
	public ConnectionState ConnectionState;
}
