package net.res0l.openra.Orders;

import com.badlogic.gdx.math.*;

import net.res0l.openra.*;
import net.res0l.openra.OpenRAFileFormat.MouseInput;
import net.res0l.openra.OpenRAGraphics.WorldRenderer;
import net.res0l.openra.OpenRANetwork.*;

public interface IOrderGenerator
{
	Iterable<Order> Order(World world, Vector2 xy, MouseInput mi);
	void Tick(World world);
	void RenderBeforeWorld(WorldRenderer wr, World world);
	void RenderAfterWorld(WorldRenderer wr, World world);
	String GetCursor(World world, Vector2 xy, MouseInput mi);
}
