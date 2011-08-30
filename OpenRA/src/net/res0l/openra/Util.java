package net.res0l.openra;

import net.res0l.openra.OpenRAGraphics.OpenRASprite;

public class Util {
	
	static int[] channelMasks = { 2, 1, 0, 3 };	// yes, our channel order is nuts.
	
	public static void FastCopyIntoChannel(OpenRASprite dest, byte[] src)
	{
		byte[] data = dest.sheet.Data();
		int srcStride = (int) dest.bounds.width;
		int destStride = dest.sheet.Size.Width * 4;
		int destOffset = (int) (destStride * dest.bounds.y + dest.bounds.x * 4 + channelMasks[dest.channel.ordinal()]);
		int destSkip = destStride - 4 * srcStride;
		int height = (int) dest.bounds.height;

		int srcOffset = 0;
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < srcStride; i++, srcOffset++)
			{
				data[destOffset] = src[srcOffset];
				destOffset += 4;
			}
			destOffset += destSkip;
		}
	}
}
