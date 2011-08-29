package net.res0l.openra.OpenRAFileFormat;

import com.badlogic.gdx.math.Vector2;

public class Vertex
{
	public float x, y, z, u, v;
	public float p, c;

	public Vertex(Vector2 xy, Vector2 uv, Vector2 pc)
	{
		this.x = xy.x; this.y = xy.y; this.z = 0;
		this.u = uv.x; this.v = uv.y;
		this.p = pc.x; this.c = pc.y;
	}
}
