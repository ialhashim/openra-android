package net.res0l.openra;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager implements ApplicationListener {
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	public void create() {
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		spriteBatch = new SpriteBatch();
		
		Game.Initialize( new Arguments(args) );
	}

	public void render() {
		
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		font.draw(spriteBatch, "Hello World!", 100, 100);
		spriteBatch.end();
		
		
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}
}
