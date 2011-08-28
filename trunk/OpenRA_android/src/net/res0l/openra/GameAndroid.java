package net.res0l.openra;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import net.res0l.openra.Game;

public class GameAndroid extends AndroidApplication {
	@Override public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new Game(), false);		
	}
}
