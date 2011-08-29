package net.res0l.openra;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyMath {
	public static float Lerp(float a, float b, float t) { return a + t * (b - a); }

	public static Vector2 Lerp(Vector2 a, Vector2 b, float t)
	{
		return new Vector2(
			Lerp(a.x, b.x, t),
			Lerp(a.y, b.y, t));
	}

	public static Vector2 Lerp(Vector2 a, Vector2 b, Vector2 t)
	{
		return new Vector2(Lerp(a.x, b.x, t.x), Lerp(a.y, b.y, t.y));
	}
	
	public static Vector2 Clamp(Vector2 pos, Rectangle r)
	{
		return new Vector2(Math.min(r.width, Math.max(pos.x, r.x)),
            Math.min(r.height, Math.max(pos.y, r.y)));
	}
}
