package com.alexian123.game;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.IniParser;
import com.alexian123.util.enums.ConfigSection;
import com.alexian123.util.enums.SettingName;
import com.alexian123.util.immutable.ImmutableMatrix4f;
import com.alexian123.util.immutable.ImmutableVector3f;
import com.alexian123.util.mathematics.MatrixCreator;

public class Settings {
	
    /* PREDEFINED SETTINGS (WILL NEVER CHANGE) -------------------------------------------- */

    // directories
    public final String modelsDir = "/res/models/";
    public final String regularModelsDir = modelsDir + "regular/";
    public final String nmModelsDir = modelsDir + "normal_mapping/";
    public final String animatedModelsDir = modelsDir + "animated/";
    public final String texturesDir = "/res/textures/";
    public final String fontsDir = "/res/fonts/";
    public final String animationsDir = animatedModelsDir;
    public final String shadersDir = "/com/alexian123/shader/";
    public final String vertexShadersDir = shadersDir + "vertex/";
    public final String fragmentShadersDir = shadersDir + "fragment/";

    // file extensions
    public final String modelFileExtension = ".obj";
    public final String animatedModelFileExtension = ".dae";
    public final String textureFileExtension = ".png";
    public final String fontFileExtension = ".fnt";
    public final String animationFileExtension = animatedModelFileExtension;
    public final String vertexShaderSuffix = "_vertex.glsl";
    public final String fragmentShaderSuffix = "_fragment.glsl";

    // other constants
    public final ImmutableMatrix4f projectionMatrix; // will be created after farPlane, nearPlane and fov are set
    public final float millisecondsPerSecond = 1000.0f;
    public final int maxLights = 4;
    public final int maxJoints = 50;
    public final int maxWeights = 3;

    /* ------------------------------------------------------------------------------------ */


    /* MODIFIABLE SETTINGS (WILL BE IMPORTED FROM FILE) ----------------------------------- */

    // window
    public final String windowTitle;
    public final int screenWidth;
    public final int screenHeight;
    public final int fpsCap;

    // engine
    public final int numMultisamples;
    public final int maxParticles;
    public final int mousePickerRecursionCount;
    public final float mousePickerRayRange;

    // camera
    public final float nearPlane;
    public final float farPlane;
    public final float fov;
    
    // physics
    public final float gravity;
    
    // player
    public final float playerRunSpeed;
    public final float playerTurnSpeed;
    public final float playerJumpPower;

    // fog
    public final ImmutableVector3f fogColor;
    public final float fogDensity;
    public final float fogGradient;

    // lighting
    public final float ambientLight;

    // post processing
    public final int blurLevel;
    public final float bloomFactor;
    public final float contrast;

    // terrain
    public final int terrainSeed;
    public final float terrainAmplitude;
    public final float terrainRoughness;
    public final int terrainOctaves;
    public final float terrainTileSize;

    // water
    public final int waterReflectionWidth;
    public final int waterReflectionHeight;
    public final int waterRefractionWidth;
    public final int waterRefractionHeight;
    public final float waterWaveSpeed;
    public final float waterShineDamper;
    public final float waterReflectivity;
    public final float waterTilingFactor;
    public final float waterWaveStrength;
    public final float waterTileSize;

    // shadows
    public final int shadowMapSize;
    public final int pcfCount;
    public final float shadowDistance;
    public final float shadowTransition;
    public final float shadowboxOffset;

    // text
    public final float defaultCharacterWidth;
    public final float defaultCharacterEdge;
    public final float defaultBorderWidth;
    public final float defaultBorderEdge;
    public final int desiredPadding;
    public final double lineHeight;

    /* ------------------------------------------------------------------------------------ */

    // Default settings
    private Settings() {
        windowTitle = "GameEngine3D";
        screenWidth = 1600;
        screenHeight = 900;
        fpsCap = 144;
        numMultisamples = 4;
        maxParticles = 10000;
        mousePickerRecursionCount = 200;
        mousePickerRayRange = 600;
        nearPlane = 0.1f;
        farPlane = 1000.0f;
        fov = 70.0f;
        projectionMatrix = new ImmutableMatrix4f(MatrixCreator.createProjectionMatrix(fov, farPlane, nearPlane, screenWidth, screenHeight));
        gravity = -50.0f;
        playerRunSpeed = 20.0f;
        playerTurnSpeed = 160.0f;
        playerJumpPower = 20.0f;
        fogColor = new ImmutableVector3f(new Vector3f(0.5444f, 0.62f, 0.69f));
        fogDensity = 0.003f;
        fogGradient = 5.0f;
        ambientLight = 0.4f;
        blurLevel = 3;
        bloomFactor = 0.5f;
        contrast = 0.3f;
        terrainSeed = 42;
        terrainAmplitude = 70f;
        terrainRoughness = 0.1f;
        terrainOctaves = 4;
        terrainTileSize = 800.0f;
        waterReflectionWidth = 800;
        waterReflectionHeight = 600;
        waterRefractionWidth = 1280;
        waterRefractionHeight = 720;
        waterWaveSpeed = 0.03f;
        waterShineDamper = 20f;
        waterReflectivity = 0.5f;
        waterTilingFactor = 4.0f;
        waterWaveStrength = 0.04f;
        waterTileSize = 60.0f;
        shadowMapSize = 4096 * 4;
        pcfCount = 4;
        shadowDistance = 200f;
        shadowTransition = 10f;
        shadowboxOffset = 20f;
        defaultCharacterWidth = 0.5f;
        defaultCharacterEdge = 0.1f;
        defaultBorderWidth = 0.0f;
        defaultBorderEdge = 0.4f;
        desiredPadding = 8;
        lineHeight = 0.03f;
    }
    
    // Import from file
    private Settings(String configFile) throws Exception {
    	IniParser parser = new IniParser(configFile);
        windowTitle = parser.get(ConfigSection.WINDOW.getValue(), SettingName.WINDOW_TITLE.getValue());
        screenWidth = Integer.parseInt(parser.get(ConfigSection.WINDOW.getValue(), SettingName.SCREEN_WIDTH.getValue()));
        screenHeight = Integer.parseInt(parser.get(ConfigSection.WINDOW.getValue(), SettingName.SCREEN_HEIGHT.getValue()));
        fpsCap = Integer.parseInt(parser.get(ConfigSection.WINDOW.getValue(), SettingName.FPS_CAP.getValue()));
        numMultisamples = Integer.parseInt(parser.get(ConfigSection.ENGINE.getValue(), SettingName.NUM_MULTISAMPLES.getValue()));
        maxParticles = Integer.parseInt(parser.get(ConfigSection.ENGINE.getValue(), SettingName.MAX_PARTICLES.getValue()));
        mousePickerRecursionCount = Integer.parseInt(parser.get(ConfigSection.ENGINE.getValue(), SettingName.MOUSE_PICKER_RECURSION_COUNT.getValue()));
        mousePickerRayRange = Float.parseFloat(parser.get(ConfigSection.ENGINE.getValue(), SettingName.MOUSE_PICKER_RAY_RANGE.getValue()));
        nearPlane = Float.parseFloat(parser.get(ConfigSection.CAMERA.getValue(), SettingName.NEAR_PLANE.getValue()));
        farPlane = Float.parseFloat(parser.get(ConfigSection.CAMERA.getValue(), SettingName.FAR_PLANE.getValue()));
        fov = Float.parseFloat(parser.get(ConfigSection.CAMERA.getValue(), SettingName.FOV.getValue()));
        projectionMatrix = new ImmutableMatrix4f(MatrixCreator.createProjectionMatrix(fov, farPlane, nearPlane, screenWidth, screenHeight));
        gravity = Float.parseFloat(parser.get(ConfigSection.PHYSICS.getValue(), SettingName.GRAVITY.getValue()));
        playerRunSpeed = Float.parseFloat(parser.get(ConfigSection.PLAYER.getValue(), SettingName.PLAYER_RUN_SPEED.getValue()));
        playerTurnSpeed = Float.parseFloat(parser.get(ConfigSection.PLAYER.getValue(), SettingName.PLAYER_TURN_SPEED.getValue()));
        playerJumpPower = Float.parseFloat(parser.get(ConfigSection.PLAYER.getValue(), SettingName.PLAYER_JUMP_POWER.getValue()));
        fogColor = new ImmutableVector3f(new Vector3f(
        	Float.parseFloat(parser.get(ConfigSection.FOG.getValue(), SettingName.FOG_COLOR_R.getValue())), 
        	Float.parseFloat(parser.get(ConfigSection.FOG.getValue(), SettingName.FOG_COLOR_G.getValue())), 
        	Float.parseFloat(parser.get(ConfigSection.FOG.getValue(), SettingName.FOG_COLOR_B.getValue()))
        ));
        fogDensity = Float.parseFloat(parser.get(ConfigSection.FOG.getValue(), SettingName.FOG_DENSITY.getValue()));
        fogGradient = Float.parseFloat(parser.get(ConfigSection.FOG.getValue(), SettingName.FOG_GRADIENT.getValue()));
        ambientLight = Float.parseFloat(parser.get(ConfigSection.LIGHTING.getValue(), SettingName.AMBIENT_LIGHT.getValue()));
        blurLevel = Integer.parseInt(parser.get(ConfigSection.POST_PROCESSING.getValue(), SettingName.BLUR_LEVEL.getValue()));
        bloomFactor = Float.parseFloat(parser.get(ConfigSection.POST_PROCESSING.getValue(), SettingName.BLOOM_FACTOR.getValue()));
        contrast = Float.parseFloat(parser.get(ConfigSection.POST_PROCESSING.getValue(), SettingName.CONTRAST.getValue()));
        terrainSeed = Integer.parseInt(parser.get(ConfigSection.TERRAIN.getValue(), SettingName.TERRAIN_SEED.getValue()));
        terrainAmplitude = Float.parseFloat(parser.get(ConfigSection.TERRAIN.getValue(), SettingName.TERRAIN_AMPLITUDE.getValue()));
        terrainRoughness = Float.parseFloat(parser.get(ConfigSection.TERRAIN.getValue(), SettingName.TERRAIN_ROUGHNESS.getValue()));
        terrainOctaves = Integer.parseInt(parser.get(ConfigSection.TERRAIN.getValue(), SettingName.TERRAIN_OCTAVES.getValue()));
        terrainTileSize = Float.parseFloat(parser.get(ConfigSection.TERRAIN.getValue(), SettingName.TERRAIN_TILE_SIZE.getValue()));
        waterReflectionWidth = Integer.parseInt(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_REFLECTION_WIDTH.getValue()));
        waterReflectionHeight = Integer.parseInt(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_REFLECTION_HEIGHT.getValue()));
        waterRefractionWidth = Integer.parseInt(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_REFRACTION_WIDTH.getValue()));
        waterRefractionHeight = Integer.parseInt(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_REFRACTION_HEIGHT.getValue()));
        waterWaveSpeed = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_WAVE_SPEED.getValue()));
        waterShineDamper = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_SHINE_DAMPER.getValue()));
        waterReflectivity = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_REFLECTIVITY.getValue()));
        waterTilingFactor = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_TILING_FACTOR.getValue()));
        waterWaveStrength = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_WAVE_STRENGTH.getValue()));
        waterTileSize = Float.parseFloat(parser.get(ConfigSection.WATER.getValue(), SettingName.WATER_TILE_SIZE.getValue()));
        shadowMapSize = Integer.parseInt(parser.get(ConfigSection.SHADOWS.getValue(), SettingName.SHADOW_MAP_SIZE.getValue()));
        pcfCount = Integer.parseInt(parser.get(ConfigSection.SHADOWS.getValue(), SettingName.PCF_COUNT.getValue()));
        shadowDistance = Float.parseFloat(parser.get(ConfigSection.SHADOWS.getValue(), SettingName.SHADOW_DISTANCE.getValue()));
        shadowTransition = Float.parseFloat(parser.get(ConfigSection.SHADOWS.getValue(), SettingName.SHADOW_TRANSITION.getValue()));
        shadowboxOffset = Float.parseFloat(parser.get(ConfigSection.SHADOWS.getValue(), SettingName.SHADOWBOX_OFFSET.getValue()));
        defaultCharacterWidth = Float.parseFloat(parser.get(ConfigSection.TEXT.getValue(), SettingName.DEFAULT_CHARACTER_WIDTH.getValue()));
        defaultCharacterEdge = Float.parseFloat(parser.get(ConfigSection.TEXT.getValue(), SettingName.DEFAULT_CHARACTER_EDGE.getValue()));
        defaultBorderWidth = Float.parseFloat(parser.get(ConfigSection.TEXT.getValue(), SettingName.DEFAULT_BORDER_WIDTH.getValue()));
        defaultBorderEdge = Float.parseFloat(parser.get(ConfigSection.TEXT.getValue(), SettingName.DEFAULT_BORDER_EDGE.getValue()));
        desiredPadding = Integer.parseInt(parser.get(ConfigSection.TEXT.getValue(), SettingName.DESIRED_PADDING.getValue()));
        lineHeight = Double.parseDouble(parser.get(ConfigSection.TEXT.getValue(), SettingName.LINE_HEIGHT.getValue()));
    }
    
    public static Settings importFrom(String configFile) {
    	Settings settings = null;
    	try {
    		settings = new Settings(configFile);
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.err.println("Error importing settings from " + configFile);
    		System.err.println("Using default settings");
    		settings = new Settings();
    	}
    	return settings;
    }
}
