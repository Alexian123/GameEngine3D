package com.alexian123.util;

import org.lwjgl.util.vector.Vector3f;

public class Constants {
	public static final float GRAVITY = -50.0f;
	
	public static final int MAX_LIGHTS = 4;
	
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000.0f;
	public static final float FOV = 70.0f;
	
	public static final Vector3f FOG_COLOR = new Vector3f(0.5444f, 0.62f, 0.69f);
	public static final float FOG_DENSITY = 0.0035f;
	public static final float FOG_GRADIENT = 5.0f;
}
