package com.alexian123.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.mathematics.MatrixCreator;

public class Constants {
	
	public static final int DEFAULT_SCREEN_WIDTH = 1600;
	public static final int DEFAULT_SCREEN_HEIGHT = 900;
	public static final String DEFAULT_WINDOW_TITLE = "GameEngine3D";
	
	public static final int FPS_CAP = 144;
	
	public static final String MODELS_DIR = "/res/models/";
	public static final String REGULAR_MODELS_DIR = MODELS_DIR + "regular/";
	public static final String NM_MODELS_DIR = MODELS_DIR + "normal_mapping/";
	public static final String ANIMATED_MODELS_DIR = MODELS_DIR + "animated/";
	public static final String TEXTURES_DIR = "/res/textures/";
	public static final String FONTS_DIR = "/res/fonts/";
	public static final String ANIMATIONS_DIR = ANIMATED_MODELS_DIR;
	public static final String SHADERS_DIR = "/com/alexian123/shader/glsl/";
	public static final String VERTEX_SHADERS_DIR = SHADERS_DIR + "vertex/";
	public static final String FRAGMENT_SHADERS_DIR = SHADERS_DIR + "fragment/";
	
	public static final String TEXTURE_FILE_TYPE = "PNG";
	
	public static final String MODEL_FILE_EXTENSION = ".obj";
	public static final String ANIMATED_MODEL_FILE_EXTENSION = ".dae";
	public static final String TEXTURE_FILE_EXTENSION = ".png";
	public static final String FONT_FILE_EXTENSION = ".fnt";
	public static final String ANIMATION_FILE_EXTENSION = ANIMATED_MODEL_FILE_EXTENSION;
	
	public static final String VERTEX_SHADER_SUFFIX = "_vertex.glsl";
	public static final String FRAGMENT_SHADER_SUFFIX = "_fragment.glsl";
	
	public static final float GRAVITY = -50.0f;
	
	public static final int MAX_LIGHTS = 4;
	public static final int MAX_PARTICLES = 10000;
	
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000.0f;
	public static final float FOV = 70.0f;
	public static final Matrix4f PROJECTION_MATRIX = MatrixCreator.createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
	
	public static final float AMBIENT_LIGHT = 0.4f;
	public static final float BLOOM_FACTOR = 0.5f;
	
	public static final Vector3f FOG_COLOR = new Vector3f(0.5444f, 0.62f, 0.69f);
	public static final float FOG_DENSITY = 0.003f;
	public static final float FOG_GRADIENT = 5.0f;
	
	public static final int SHADOW_MAP_SIZE = 4096 * 4;
	public static final int PCF_COUNT = 4;
	public static final float SHADOW_DISTANCE = 200f;
	public static final float SHADOW_TRANSITION = 10f;
	public static final float SHADOWBOX_OFFSET = 20f;
	
	public static final int BLUR_LEVEL = 5;
	
	public static final int NUM_MULTISAMPLES = 4;
	
	public static final int MAX_JOINTS = 50;
	public static final int MAX_WEIGHTS = 3;
	
	public static final float MILLISECONDS_PER_SECOND = 1000.0f;
}
