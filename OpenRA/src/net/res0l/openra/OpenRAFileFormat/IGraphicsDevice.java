package net.res0l.openra.OpenRAFileFormat;

import net.res0l.openra.Action;
import net.res0l.openra.Bitmap;
import net.res0l.openra.InputHandler.DefaultInputHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public interface IGraphicsDevice {

	IVertexBuffer<Vertex> CreateVertexBuffer( int length );
	IIndexBuffer CreateIndexBuffer( int length );
	ITexture CreateTexture( Image image );
	ITexture CreateTexture();
	IShader CreateShader( String name );

	Vector2 WindowSize = new Vector2();
	int GpuMemoryUsed = 0;

	void Clear( Color color );
	void Present( DefaultInputHandler defaultInputHandler );

	void DrawIndexedPrimitives( PrimitiveType type, Range<Integer> vertexRange, Range<Integer> indexRange );
	void DrawIndexedPrimitives( PrimitiveType type, int vertexPool, int numPrimitives );

	void EnableScissor( int left, int top, int width, int height );
	void DisableScissor();

	public interface IVertexBuffer<T>
	{
		void Bind();
		void SetData( T[] vertices, int length );
	}

	public interface IIndexBuffer
	{
		void Bind();
		void SetData( long[] indices, int length );
	}

	public interface IShader
	{
		void SetValue( String name, float x, float y );
		void SetValue( String param, ITexture texture );
		void Render( Action a );
	}

	public interface ITexture
	{
		void SetData(Bitmap bitmap);
		void SetData(long[][] colors);
		void SetData(byte[] colors, int width, int height);
	}

    public enum PrimitiveType
    {
        PointList, 
        LineList, 
        TriangleList,
    }

	void Present(IInputHandler inputHandler);
}
