package net.res0l.openra.OpenRAFileFormat;

import net.res0l.openra.MyMath;

import com.badlogic.gdx.graphics.Color;

public class ColorRamp
{
    public int H,S,L,R;

    public ColorRamp(int h, int s, int l, int r)
    {
        H = h; S = s; L = l; R = r;
    }

    /* returns a color along the Lum ramp */
    public Color GetColor( float t )
    {
        return ColorFromHSL( H / 255f, S / 255f, MyMath.Lerp( L / 255f, L*R / (255f * 255f), t ) );
    }

    public String ToString()
    {
        return String.format("%d %d %d %d", H, S, L, R);
    }

    // hk is hue in the range [0,1] instead of [0,360]
	public static Color ColorFromHSL(float hk, float s, float l)
	{
		// Convert from HSL to RGB
		float q = (l < 0.5f) ? l * (1 + s) : l + s - (l * s);
		float p = 2 * l - q;

		float[] trgb = { hk + 1 / 3.0f,
						  hk,
						  hk - 1/3.0f };
		float[] rgb = { 0, 0, 0 };

		for (int k = 0; k < 3; k++)
		{
			while (trgb[k] < 0) trgb[k] += 1.0f;
			while (trgb[k] > 1) trgb[k] -= 1.0f;
		}

		for (int k = 0; k < 3; k++)
		{
			if (trgb[k] < 1 / 6.0f) { rgb[k] = (p + ((q - p) * 6 * trgb[k])); }
			else if (trgb[k] >= 1 / 6.0f && trgb[k] < 0.5) { rgb[k] = q; }
			else if (trgb[k] >= 0.5f && trgb[k] < 2.0f / 3) { rgb[k] = (p + ((q - p) * 6 * (2.0f / 3 - trgb[k]))); }
			else { rgb[k] = p; }
		}

		return new Color(rgb[0], rgb[0], rgb[0], 1.0f);
	}
}