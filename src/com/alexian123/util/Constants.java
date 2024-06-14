package com.alexian123.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Constants {
	
	public static final int DEFAULT_SCREEN_WIDTH = 1600;
	public static final int DEFAULT_SCREEN_HEIGHT = 900;
	public static final String DEFAULT_WINDOW_TITLE = "GameEngine3D";
	
	public static final int FPS_CAP = 144;
	
	public static final float GRAVITY = -50.0f;
	
	public static final int MAX_LIGHTS = 4;
	public static final int MAX_PARTICLES = 10000;
	
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000.0f;
	public static final float FOV = 70.0f;
	public static final Matrix4f PROJECTION_MATRIX = Maths.createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
	
	public static final Vector3f FOG_COLOR = new Vector3f(0.5444f, 0.62f, 0.69f);
	public static final float FOG_DENSITY = 0.0035f;
	public static final float FOG_GRADIENT = 5.0f;
	
	public static final int SHADOW_MAP_SIZE = 4096 * 4;
	public static final int PCF_COUNT = 4;
	public static final float SHADOW_DISTANCE = 200f;
	public static final float SHADOW_TRANSITION = 10f;
	public static final float SHADOWBOX_OFFSET = 20f;
	
	public static final float MILLISECONDS_PER_SECOND = 1000.0f;
}
