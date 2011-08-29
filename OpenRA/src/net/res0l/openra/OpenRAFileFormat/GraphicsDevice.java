package net.res0l.openra.OpenRAFileFormat;

import net.res0l.openra.InputHandler.DefaultInputHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class GraphicsDevice implements IGraphicsDevice {

	@Override
	public IVertexBuffer<Vertex> CreateVertexBuffer(int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIndexBuffer CreateIndexBuffer(int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITexture CreateTexture(Image image) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITexture CreateTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IShader CreateShader(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Clear(Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Present(IInputHandler inputHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DrawIndexedPrimitives(PrimitiveType type,
			Range<Integer> vertexRange, Range<Integer> indexRange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DrawIndexedPrimitives(PrimitiveType type, int vertexPool,
			int numPrimitives) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void EnableScissor(int left, int top, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DisableScissor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Present(DefaultInputHandler defaultInputHandler) {
		// TODO Auto-generated method stub
		
	}

}
