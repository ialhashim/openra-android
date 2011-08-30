package net.res0l.openra;

import com.badlogic.gdx.graphics.Texture;

public class Bitmap extends Texture {

	public Size Size;
	
	public Bitmap(String internalPath) {
		super(internalPath);
		
		Size = new Size(getWidth(), getHeight());
	}
}
