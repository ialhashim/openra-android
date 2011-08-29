package net.res0l.openra.OpenRAGraphics;

import java.util.PriorityQueue;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.res0l.openra.InputHandler.DefaultInputHandler;
import net.res0l.openra.OpenRAFileFormat.Graphics.WindowMode;
import net.res0l.openra.OpenRAFileFormat.GraphicsDevice;
import net.res0l.openra.OpenRAFileFormat.IGraphicsDevice;
import net.res0l.openra.OpenRAFileFormat.Vertex;
import net.res0l.openra.OpenRAFileFormat.IGraphicsDevice.*;

public class Renderer {

	public Vector2 Resolution = new Vector2();

	public int SheetSize;

	IShader SpriteShader;    /* note: shared shader params */
	IShader LineShader;
	IShader RgbaSpriteShader;
	IShader WorldSpriteShader;

	public static SpriteBatch batch;
	
	public SpriteRenderer SpriteRenderer;
	public SpriteRenderer RgbaSpriteRenderer;
	public SpriteRenderer WorldSpriteRenderer;
	public LineRenderer LineRenderer;

	public ITexture PaletteTexture;

    public BitmapFont RegularFont, BoldFont, TitleFont, TinyFont, TinyBoldFont;

	static int TempBufferSize = 8192;
	int TempBufferCount = 8;

	Queue<IVertexBuffer<Vertex>> tempBuffersV = new PriorityQueue<IVertexBuffer<Vertex>>();
	Queue<IIndexBuffer> tempBuffersI = new PriorityQueue<IIndexBuffer>();

	public Renderer()
	{
		device = new GraphicsDevice();
		
		SpriteShader = device.CreateShader("world-shp");
		LineShader = device.CreateShader("world-line");
		RgbaSpriteShader = device.CreateShader("chrome-rgba");
		WorldSpriteShader = device.CreateShader("chrome-shp");

		SpriteRenderer = new SpriteRenderer( this, SpriteShader );
		RgbaSpriteRenderer = new SpriteRenderer( this, RgbaSpriteShader );
		WorldSpriteRenderer = new SpriteRenderer( this, WorldSpriteShader );
		LineRenderer = new LineRenderer(this);

		RegularFont = new BitmapFont(Gdx.files.internal("data/regularFont.fnt"), false);
		BoldFont = new BitmapFont(Gdx.files.internal("data/boldFont.fnt"), false);
		TitleFont = new BitmapFont(Gdx.files.internal("data/titlesFont.fnt"), false);
        TinyFont = new BitmapFont(Gdx.files.internal("data/tinyFont.fnt"), false);
		TinyBoldFont =  new BitmapFont(Gdx.files.internal("data/tinyBoldFont.fnt"), false);
		
		//for( int i = 0 ; i < TempBufferCount ; i++ )
		//{
		//	tempBuffersV.Enqueue( device.CreateVertexBuffer( TempBufferSize ) );
		//	tempBuffersI.Enqueue( device.CreateIndexBuffer( TempBufferSize ) );
		//}
	}

	static IGraphicsDevice device;

	IGraphicsDevice Device;

	public void BeginFrame(Vector2 scroll)
	{
		device.Clear(Color.BLACK);

		Vector2 r1 = new Vector2(2f/Resolution.x, -2f/Resolution.y);
		Vector2 r2 = new Vector2(-1, 1);

		SetShaderParams( SpriteShader, r1, r2, scroll );
		SetShaderParams( LineShader, r1, r2, scroll );
		SetShaderParams( RgbaSpriteShader, r1, r2, scroll );
		SetShaderParams( WorldSpriteShader, r1, r2, scroll );
	}

	private void SetShaderParams( IShader s, Vector2 r1, Vector2 r2, Vector2 scroll )
	{
		s.SetValue( "Palette", PaletteTexture );
		s.SetValue( "Scroll", (int) scroll.x, (int) scroll.y );
		s.SetValue( "r1", r1.x, r1.y );
		s.SetValue( "r2", r2.x, r2.y );
	}

	public void EndFrame( DefaultInputHandler defaultInputHandler )
	{
		Flush();
		device.Present( defaultInputHandler );
	}

	/*public void DrawBatch<T>(IVertexBuffer<T> vertices, IIndexBuffer indices,
		Range<Integer> vertexRange, Range<Integer> indexRange, PrimitiveType type, IShader shader)
		where T : struct
	{
		vertices.Bind();
		indices.Bind();

		device.DrawIndexedPrimitives(type, vertexRange, indexRange);

		PerfHistory.Increment("batches", 1);
	}

	public void DrawBatch<T>(IVertexBuffer<T> vertices, IIndexBuffer indices,
		int vertexPool, int numPrimitives, PrimitiveType type)
		where T : struct
	{
		vertices.Bind();
		indices.Bind();

		device.DrawIndexedPrimitives(type, vertexPool, numPrimitives);

		PerfHistory.Increment("batches", 1);
	}*/
	
	public void Flush()
	{
		//CurrentBatchRenderer = null;
	}

	public void Initialize( WindowMode windowMode )
	{
		//var resolution = GetResolution( windowMode );
		//device = CreateDevice( Assembly.LoadFile( Path.GetFullPath( "OpenRA.Renderer.{0}.dll".F(Game.Settings.Graphics.Renderer) ) ), resolution.Width, resolution.Height, windowMode, false );
	}

	/*static Vector2 GetResolution( WindowMode windowmode )
	{
		Vector2 desktopResolution = Screen.PrimaryScreen.Bounds.Size;
		Vector2 customSize = (windowmode == WindowMode.Windowed) ? Game.Settings.Graphics.WindowedSize : Game.Settings.Graphics.FullscreenSize;
		
		if (customSize.X > 0 && customSize.Y > 0)
		{
			desktopResolution.Width = customSize.X;
			desktopResolution.Height = customSize.Y;
		}
		return new Size(
			desktopResolution.Width,
			desktopResolution.Height);
	}*/

	/*static IGraphicsDevice CreateDevice( Assembly rendererDll, int width, int height, WindowMode window, bool vsync )
	{
		foreach( RendererAttribute r in rendererDll.GetCustomAttributes( typeof( RendererAttribute ), false ) )
		{
			return (IGraphicsDevice)r.Type.GetConstructor( new Type[] { typeof( int ), typeof( int ), typeof( WindowMode ), typeof( bool ) } )
				.Invoke( new object[] { width, height, window, vsync } );
		}
		throw new NotImplementedException();
	}*/

	/*IVertexBuffer<Vertex> GetTempVertexBuffer()
	{
		var ret = tempBuffersV.Dequeue();
		tempBuffersV.Enqueue( ret );
		return ret;
	}

	internal IIndexBuffer GetTempIndexBuffer()
	{
		var ret = tempBuffersI.Dequeue();
		tempBuffersI.Enqueue( ret );
		return ret;
	}*/

	public interface IBatchRenderer
	{
		void Flush();
	}

	static IBatchRenderer currentBatchRenderer;
	/*public static IBatchRenderer CurrentBatchRenderer
	{
		get { return currentBatchRenderer; }
		set
		{
			if( currentBatchRenderer == value ) return;
			if( currentBatchRenderer != null )
				currentBatchRenderer.Flush();
			currentBatchRenderer = value;
		}
	}*/

	public void EnableScissor(int left, int top, int width, int height)
	{
		Flush();
		Device.EnableScissor( left, top, width, height );
	}

	public void DisableScissor()
	{
		Flush();
		Device.DisableScissor();
	}
}
