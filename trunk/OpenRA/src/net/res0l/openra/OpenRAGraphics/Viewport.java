package net.res0l.openra.OpenRAGraphics;

import net.res0l.openra.Game;
import net.res0l.openra.MyMath;
import net.res0l.openra.World;
import net.res0l.openra.InputHandler.DefaultInputHandler;
import net.res0l.openra.OpenRAFileFormat.IInputHandler;
import net.res0l.openra.OpenRAFileFormat.MouseInput;
import net.res0l.openra.OpenRAWidget.Widget;
import net.res0l.openra.OpenRAWidget.Widget.ScrollDirection;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Viewport
{
	Vector2 screenSize;
	Vector2 scrollPosition;
	Renderer renderer;
	Rectangle adjustedMapBounds;

	public Vector2 Location() { return scrollPosition; }

	public int Width() { return (int) screenSize.x; }
	public int Height() { return (int) screenSize.y; }

	float cursorFrame = 0f;

	public static int TicksSinceLastMove = 0;
	public static Vector2 LastMousePos;

	public void Scroll(Vector2 delta)
	{
		this.Scroll(delta, false);
	}
	
	public void Scroll(Vector2 delta, boolean ignoreBorders)
	{
		Vector2 newScrollPosition = scrollPosition.add(delta);
		
		if(!ignoreBorders)
			newScrollPosition = this.NormalizeScrollPosition(newScrollPosition);

		scrollPosition = newScrollPosition;
	}
	
	private Vector2 NormalizeScrollPosition(Vector2 newScrollPosition)
	{
		return MyMath.Clamp(newScrollPosition, adjustedMapBounds);
	}
	
	public ScrollDirection GetBlockedDirections()
	{
		ScrollDirection blockedDirections = new ScrollDirection();
		if(scrollPosition.y <= adjustedMapBounds.y)			blockedDirections.Up = true;
		if(scrollPosition.x <= adjustedMapBounds.x)			blockedDirections.Left = true;
		if(scrollPosition.y >= adjustedMapBounds.height)	blockedDirections.Down = true;
	  	if(scrollPosition.x >= adjustedMapBounds.width)		blockedDirections.Right = true;
		return blockedDirections;
	}

	public Viewport(Vector2 screenSize, Rectangle mapBounds, Renderer renderer)
	{
		this.screenSize = screenSize;
		this.renderer = renderer;
		this.adjustedMapBounds = new Rectangle(Game.CellSize()*mapBounds.x - screenSize.x/2,
		                                       Game.CellSize()*mapBounds.y - screenSize.y/2,
		                                       Game.CellSize()*mapBounds.width,
		                                       Game.CellSize()*mapBounds.height);
		
		Vector2 amb = new Vector2();
		
		this.scrollPosition = amb.add(amb.mul(0.5f));
	}
	
	public void DrawRegions( WorldRenderer wr, DefaultInputHandler defaultInputHandler )
	{
		renderer.BeginFrame(scrollPosition);
		
		if (wr != null)
			wr.Draw();
		
		Widget.DoDraw();
		
		/*String cursorName = Widget.RootWidget.GetCursorOuter(Viewport.LastMousePos);
		String cursorSequence = CursorProvider.GetCursorSequence(cursorName);

        cursorSequence.GetSprite((int)cursorFrame).DrawAt(
            Viewport.LastMousePos + Location - cursorSequence.Hotspot,
            Game.modData.Palette.GetPaletteIndex(cursorSequence.Palette));*/

		renderer.EndFrame( defaultInputHandler );
	}

	public void Tick()
	{
		cursorFrame += 0.5f;
	}

	public Vector2 ViewToWorld(Vector2 loc)
	{
		return loc.add(Location()).mul((1f / Game.CellSize()));
	}
	public Vector2 ViewToWorld(MouseInput mi)
	{
		return ViewToWorld(mi.Location);
	}
	
	public void Center(Vector2 loc)
	{
		scrollPosition = this.NormalizeScrollPosition((loc.mul(Game.CellSize()).sub(screenSize.mul(0.5f))));
	}

	/*public void Center(IEnumerable<Actor> actors)
	{
		if (!actors.Any()) return;

		var avgPos = actors.Select(a => a.CenterLocation).Aggregate((a, b) => a + b) / actors.Count();
		scrollPosition = this.NormalizeScrollPosition((avgPos - screenSize / 2));
	}*/
	
	public Rectangle ViewBounds(World world)
	{
		Rectangle r = WorldBounds(world);
		int left = (int)(Game.CellSize() * r.x - Game.viewport.Location().x);
		int top = (int)(Game.CellSize() * r.y - Game.viewport.Location().y);
		int right = left + (int)(Game.CellSize() * r.width);
		int bottom = top + (int)(Game.CellSize() * r.height);
		
		if (left < 0) left = 0;
		if (top < 0) top = 0;
		if (right > Game.viewport.Width()) right = Game.viewport.Width();
		if (bottom > Game.viewport.Height()) bottom = Game.viewport.Height();
		return new Rectangle(left, top, right - left, bottom - top);
	}

	Vector2 cachedScroll = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);
	Rectangle cachedRect;
	
	public Rectangle WorldBounds(World world)
	{
		if (cachedScroll != scrollPosition)
		{
			Vector2 boundary = new Vector2(1,1); // Add a curtain of cells around the viewport to account for rounding errors
			Vector2 tl = ViewToWorld(new Vector2()).sub(boundary);
			Vector2 br = ViewToWorld(new Vector2(Width(), Height())).add(boundary);
			//cachedRect = Rectangle.Intersect(Rectangle.FromLTRB(tl.X, tl.Y, br.X, br.Y), world.Map.Bounds);
			cachedScroll = scrollPosition;
		}
		
		//var b = world.LocalShroud.Bounds;
		//return (b.HasValue) ? Rectangle.Intersect(cachedRect, b.Value) : cachedRect;
		
		return new Rectangle();
	}
}
