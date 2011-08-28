package net.res0l.openra.InputHandler;

import net.res0l.openra.World;


public class DefaultInputHandler {
	
	public enum MouseButton
	{
		None  (0),
		Left  (1),
		Right  (2),
		Middle  (4),
		WheelDown  (8),
		WheelUp  (16);
		
	    private final int id;
	    MouseButton(int id) { this.id = id; }
	    public int getValue() { return id; }
	}
	
	public enum Modifiers {
		None (0),
		Shift (1),
		Alt (2),
		Ctrl (4),
		Meta (8);

	    private final int id;
	    Modifiers(int id) { this.id = id; }
	    public int getValue() { return id; }
	}
	
	public DefaultInputHandler(World world) {
		// TODO Auto-generated constructor stub
	}


}
