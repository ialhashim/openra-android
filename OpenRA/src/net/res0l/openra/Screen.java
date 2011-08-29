package net.res0l.openra;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Screen 
{
	private Game g;
	public SpriteBatch spriteBatch;

	public void removed() {
		spriteBatch.dispose();
	}

	public final void init(Game g) {
		this.g = g;
	}

	protected void setScreen(Screen screen) {
		
	}

	public void draw(TextureRegion region, int x, int y) 
	{
		//spriteBatch.draw(region, x, y, width, -region.getRegionHeight());
	}

	public abstract void render();

	public void tick(Input input)
	{
		
	}
}
